package com.community.file.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.common.query.dto.CommonResponse;
import com.community.file.command.application.FileService;

@RestController
@RequestMapping("/api/file/{fileId}")
public class FileRestController {
	@Autowired
	private FileService fileService;

	// 삭제 테스트
	@DeleteMapping
	public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("fileId") Integer fileId) {
		fileService.deleteFile(fileId);
		CommonResponse<Void> commonResponse = CommonResponse.success("파일 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}
}
