package com.community.comment.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.account.application.dto.AccountDto;
import com.community.comment.application.CommentService;
import com.community.comment.domain.Comment;
import com.community.common.presentation.dto.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "댓글 관련 API")
@RestController
@RequestMapping("/api/comment")
public class CommentRestController {

	@Autowired
	private CommentService commentService;

	// 댓글 생성
	@PostMapping("/auth")
	@Operation(summary = "댓글 생성")
	public ResponseEntity<CommonResponse<Comment>> createComment(@RequestParam("postId") int postId,
			@RequestParam(name = "parentId", required = false) Integer parentId,
			@RequestParam("contents") String contents, @AuthenticationPrincipal AccountDto account) {
		Comment comment = commentService.createComment(account.getId(), postId, parentId, contents);
		CommonResponse<Comment> commonResponse = CommonResponse.success("댓글 생성 성공", comment);
		return ResponseEntity.ok(commonResponse);
	}

	// 댓글 수정
	@PutMapping("/auth/{commentId}")
	@PreAuthorize("@accessControl.canAccessComment(#commentId, authentication)")
	@Operation(summary = "댓글 수정")
	public ResponseEntity<CommonResponse<Comment>> updateComment(
			@P("commentId") @PathVariable("commentId") int commentId, @RequestParam("contents") String contents) {
		Comment comment = commentService.updateCommentById(commentId, contents);
		CommonResponse<Comment> commonResponse = CommonResponse.success("댓글 수정 성공", comment);
		return ResponseEntity.ok(commonResponse);
	}

	// 댓글 삭제
	@DeleteMapping("/auth/{commentId}")
	@PreAuthorize("@accessControl.canAccessComment(#commentId, authentication)")
	@Operation(summary = "댓글 삭제")
	public ResponseEntity<CommonResponse<Comment>> deleteCommentByIdAndAccountId(
			@P("commentId") @PathVariable("commentId") int commentId) {
		commentService.deleteCommentById(commentId);
		CommonResponse<Comment> commonResponse = CommonResponse.success("댓글 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

}
