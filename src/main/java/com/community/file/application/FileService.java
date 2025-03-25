package com.community.file.application;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.community.file.FileDeleteException;
import com.community.file.FileUploadException;
import com.community.file.application.dto.FileDto;
import com.community.file.application.dto.MetadataDto;
import com.community.file.domain.File;
import com.community.file.domain.FileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

	private static final String IMAGE_FOLDER = "images";
	private static final String DOCUMENT_FOLDER = "documents";

	private final AmazonS3 amazonS3;
	private final FileRepository fileRepository;
	private final ObjectMapper objectMapper;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	// ********** Public 메서드 **********

	// 파일 업로드
	public void uploadFiles(MultipartFile[] files, String domain, Integer domainId) {
		for (MultipartFile file : files) {
			File savedFile = saveFileInfo(file, domain, domainId);
			uploadToS3(file).thenAccept(fileUrl -> updateFileUrl(savedFile.getId(), fileUrl))
					.exceptionally(e -> handleUploadFailure(savedFile.getId(), e));
		}
	}

	// 파일 목록 조회
	public List<File> getFilesByDomainAndDomainId(String domain, Integer domainId) {
		return fileRepository.findByDomainAndDomainId(domain, domainId);
	}

	// 파일 삭제
	public void deleteFile(Integer fileId) {
		File savedFile = fileRepository.findById(fileId)
				.orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다. ID: " + fileId));
		deleteFromS3(savedFile.getUrl());
		fileRepository.delete(savedFile);
	}

	// 여러 개의 파일 삭제
	public void deleteFiles(List<Integer> fileIdList) {
		for (Integer fileId : fileIdList) {
			try {
				deleteFile(fileId);
			} catch (EntityNotFoundException e) {
				log.warn("삭제할 파일을 찾을 수 없음: {}", fileId);
			} catch (Exception e) {
				log.error("파일 삭제 실패: {}", fileId, e);
			}
		}
	}

	// ********** Private 메서드 **********

	// 파일 정보 저장
	private File saveFileInfo(MultipartFile file, String domain, Integer domainId) {
		try {
			FileDto fileDto = FileDto.builder().domain(domain).domainId(domainId).type(file.getContentType())
					.size(file.getSize()).metadata(createFileMetadata(file)).build();
			return fileRepository.save(fileDto.toEntity());
		} catch (Exception e) {
			log.error("파일 정보 저장 실패: {}", file.getOriginalFilename(), e);
			throw new FileUploadException("파일 정보 저장 실패");
		}
	}

	// 파일 URL 업데이트
	private void updateFileUrl(Integer fileId, String fileUrl) {
		File file = fileRepository.findById(fileId)
				.orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다. ID: " + fileId));
		File updatedFile = file.toBuilder().url(fileUrl).build();
		fileRepository.save(updatedFile);
	}

	// 업로드 실패 처리
	private Void handleUploadFailure(Integer fileId, Throwable e) {
		log.error("파일 업로드 실패 (fileId={}): {}", fileId, e.getMessage(), e);
		deleteFile(fileId);
		throw new FileUploadException("파일 업로드 실패");
	}

	// S3에 파일 업로드
	@Async
	private CompletableFuture<String> uploadToS3(MultipartFile file) {
		String key = generateKey(file);
		ObjectMetadata metadata = createObjectMetadata(file);

		try {
			amazonS3.putObject(bucketName, key, file.getInputStream(), metadata);
		} catch (Exception e) {
			log.error("S3 업로드 실패: {}", e.getMessage(), e);
			throw new FileUploadException("S3 업로드 실패");
		}

		String fileUrl = amazonS3.getUrl(bucketName, key).toString();
		return CompletableFuture.completedFuture(fileUrl);
	}

	// S3에서 파일 삭제
	private void deleteFromS3(String fileUrl) {
		String fileKey = fileUrl.substring(fileUrl.indexOf(".com/") + 5);
		try {
			amazonS3.deleteObject(bucketName, fileKey);
		} catch (Exception e) {
			throw new FileDeleteException("S3 삭제 실패");
		}
	}

	// S3 파일 키 생성
	private String generateKey(MultipartFile file) {
		String folder = getFolderByContentType(file.getContentType());
		return folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
	}

	// 메타데이터 생성
	private String createFileMetadata(MultipartFile file) {
		try {
			MetadataDto metadata = MetadataDto.builder().contentType(file.getContentType())
					.originalFilename(file.getOriginalFilename()).build();
			return objectMapper.writeValueAsString(metadata);
		} catch (Exception e) {
			log.error("메타데이터 생성 실패: {}", file.getOriginalFilename(), e);
			throw new FileUploadException("메타데이터 생성 실패");
		}
	}

	// S3 메타데이터 생성
	private ObjectMetadata createObjectMetadata(MultipartFile file) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());
		return metadata;
	}

	// 파일 타입별 저장 폴더 선택
	private String getFolderByContentType(String contentType) {
		return contentType.startsWith("image/") ? IMAGE_FOLDER : DOCUMENT_FOLDER;
	}
}
