package com.community.file.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.community.file.application.dto.MetadataDto;
import com.community.file.domain.File;
import com.community.file.domain.FileRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileService {

	private final AmazonS3 amazonS3;
	private final FileRepository fileRepository;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	public String uploadFile(MultipartFile file, String domain, Integer domainId) {
		// 파일 업로드 처리
		String folder = file.getContentType().startsWith("image/") ? "images" : "documents";
		String fileName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

		// 메타데이터 설정
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());

		// S3에 파일 업로드
		try {
			amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
		} catch (AmazonServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SdkClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();

		// 파일 메타데이터 저장
		File fileEntity = File.builder().url(fileUrl).domain(domain).domainId(domainId).type(file.getContentType())
				.size(file.getSize()).metadata(createFileMetadata(file)).build();
		fileRepository.save(fileEntity);

		return fileUrl;
	}

	public List<String> uploadFiles(MultipartFile[] files, String domain, Integer domainId) {
		List<String> fileUrls = new ArrayList<>();
		for (MultipartFile file : files) {
			fileUrls.add(uploadFile(file, domain, domainId));
		}
		return fileUrls;
	}

	public List<File> getFilesByDomainAndDomainId(String domain, Integer domainId) {
		return fileRepository.findByDomainAndDomainId(domain, domainId);
	}

	public void deleteFile(Integer fileId) {
		File savedFile = fileRepository.findById(fileId)
				.orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다. ID: " + fileId));
		String fileKey = savedFile.getUrl().substring(savedFile.getUrl().indexOf(".com/") + 5);
		amazonS3.deleteObject(bucketName, fileKey);
		fileRepository.delete(savedFile);
	}

	public void deleteFiles(Integer[] fileIdList) {
		for (Integer fileId : fileIdList) {
			deleteFile(fileId);
		}
	}

	private String createFileMetadata(MultipartFile file) {
		try {
			MetadataDto metadata = new MetadataDto();
			metadata.setContentType(file.getContentType());
			metadata.setOriginalFilename(file.getOriginalFilename());
			ObjectMapper objectMapper = new ObjectMapper();
			return objectMapper.writeValueAsString(metadata);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to create file metadata JSON");
		}
	}

}
