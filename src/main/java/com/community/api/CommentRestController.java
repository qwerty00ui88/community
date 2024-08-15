package com.community.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.aop.LoginCheck;
import com.community.dto.response.CommonResponse;
import com.community.entity.CommentEntity;
import com.community.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "댓글 관련 API")
@RestController
@RequestMapping("/api/comment")
public class CommentRestController {

	@Autowired
	private CommentService commentService;

	// 댓글 생성
	@LoginCheck
	@PostMapping
	@Operation(summary = "댓글 생성")
	public ResponseEntity<CommonResponse<CommentEntity>> createComment(
			@RequestParam(name = "id", required = false) Integer userId, @RequestParam("postId") int postId,
			@RequestParam(name = "parentId", required = false) Integer parentId,
			@RequestParam("contents") String contents) {
		CommentEntity comment = commentService.createComment(userId, postId, parentId, contents);
		CommonResponse<CommentEntity> commonResponse = CommonResponse.success("댓글 생성 성공", comment);
		return ResponseEntity.ok(commonResponse);
	}

	// 댓글 수정
	@LoginCheck
	@PutMapping("/{commentId}")
	@Operation(summary = "댓글 수정")
	public ResponseEntity<CommonResponse<CommentEntity>> updateComment(
			@RequestParam(name = "id", required = false) Integer userId, @PathVariable("commentId") int commentId,
			@RequestParam("contents") String contents) {
		CommentEntity comment = commentService.updateCommentByIdAndUserId(commentId, userId, contents);
		CommonResponse<CommentEntity> commonResponse = CommonResponse.success("댓글 수정 성공", comment);
		return ResponseEntity.ok(commonResponse);
	}

	// 댓글 삭제
	@LoginCheck
	@DeleteMapping("/{commentId}")
	@Operation(summary = "댓글 삭제")
	public ResponseEntity<CommonResponse<CommentEntity>> deleteCommentByIdAndUserId(
			@RequestParam(name = "id", required = false) Integer userId, @PathVariable("commentId") int commentId) {
		commentService.deleteCommentByIdAndUserId(commentId, userId);
		CommonResponse<CommentEntity> commonResponse = CommonResponse.success("댓글 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

	// 관라자용 댓글 삭제
	@LoginCheck(type = LoginCheck.UserType.ADMIN)
	@DeleteMapping("/admin/{commentId}")
	@Operation(summary = "(관리자용) 댓글 삭제")
	public ResponseEntity<CommonResponse<Void>> deleteCommentById(
			@RequestParam(name = "id", required = false) Integer userId, @PathVariable("commentId") int commentId) {
		commentService.deleteCommentById(commentId);
		CommonResponse<Void> commonResponse = CommonResponse.success("댓글 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

}
