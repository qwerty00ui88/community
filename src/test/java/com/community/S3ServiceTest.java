package com.community;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.community.common.service.S3Service;

@SpringBootTest
class S3ServiceTest {
	@Value("${aws.s3.bucket-name}")
    private String bucketName;
	
    @Autowired
    private S3Service s3Service;

    @Autowired
    private AmazonS3 amazonS3;

    private final String testFolder = "test-folder";

    @AfterEach
    void cleanUp() {
        // 테스트 종료 후 업로드된 파일 삭제
        amazonS3.listObjects(bucketName, testFolder)
                .getObjectSummaries()
                .forEach(obj -> amazonS3.deleteObject(bucketName, obj.getKey()));
    }

    @Test
    void testUploadFile() throws Exception {
    	// Given
        String folder = "test-folder";
        String fileName = "test-file.txt";
        MultipartFile file = new MockMultipartFile(
            fileName,
            fileName,
            "text/plain",
            "This is a test file.".getBytes()
        );

        // When
        String uploadedUrl = s3Service.uploadFile(file);

        // Then
        assertNotNull(uploadedUrl);
        System.out.println("Uploaded URL: " + uploadedUrl);

        // Get the key and download the file
        String key = folder + "/" + fileName;
        System.out.println("Generated Key: " + key);

        S3Object s3Object = amazonS3.getObject(bucketName, key); // 요청 키 확인
        assertNotNull(s3Object);

        String downloadedContent = new String(s3Object.getObjectContent().readAllBytes());
        System.out.println("Downloaded Content: " + downloadedContent);

        assertEquals("This is a test file.", downloadedContent);
    }

}
