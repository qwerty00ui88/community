package com.community.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class S3Service {
	private final AmazonS3 amazonS3;

	@Value("${aws.s3.bucket-name}")
	private String bucketName;

	public String uploadFile(MultipartFile file) throws IOException {
		String folder = determineFolder(file);
		String fileName = folder + "/" + System.currentTimeMillis() + "_" + file.getOriginalFilename();
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(file.getSize());
		amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
		return amazonS3.getUrl(bucketName, fileName).toString();
	}

	public void deleteFile(String fileUrl) {
		String fileKey = extractFileKey(fileUrl);
		amazonS3.deleteObject(bucketName, fileKey);
	}

	public String updateFile(MultipartFile newFile, String oldFileUrl) throws IOException {
		deleteFile(oldFileUrl);
		return uploadFile(newFile);
	}

	private String determineFolder(MultipartFile file) {
		String contentType = file.getContentType();
		if (contentType != null && contentType.startsWith("image/")) {
			return "images";
		} else {
			return "documents";
		}
	}

	private String extractFileKey(String fileUrl) {
		return fileUrl.substring(fileUrl.indexOf(".com/") + 5); // Extract the S3 key from the URL
	}
}
