package com.community.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.aop.LoginCheck;
import com.community.dto.CommentDTO;
import com.community.dto.UserDTO;
import com.community.dto.response.CommonResponse;
import com.community.entity.CommentEntity;
import com.community.service.impl.CommentServiceImpl;
import com.community.service.impl.UserServiceImpl;

@RequestMapping("/api/comment")
@RestController
public class CommentRestController {

	private final CommentServiceImpl commentService;
	private final UserServiceImpl userService;

	public CommentRestController(CommentServiceImpl commentService, UserServiceImpl userService) {
		this.commentService = commentService;
		this.userService = userService;
	}

	// 댓글 생성
	@PostMapping
	@LoginCheck
	public ResponseEntity<CommonResponse<CommentEntity>> createComment(
			@RequestParam(name = "id", required = false) Integer userId, 
			@RequestParam("postId") int postId,
			@RequestParam(name = "parentId", required = false) Integer parentId,
			@RequestParam("contents") String contents) {
		CommentEntity comment = commentService.createComment(userId, postId, parentId, contents);
		CommonResponse commonResponse = new CommonResponse<CommentEntity>(HttpStatus.OK, "SUCCESS", "댓글 생성 성공",
				comment);
		return ResponseEntity.ok(commonResponse);
	}

	// 댓글 수정
	@LoginCheck
	@PutMapping("/{commentId}")
	public ResponseEntity<CommonResponse> updateComment(
			@RequestParam(name = "id", required = false) Integer userId,
			@PathVariable("commentId") int commentId, 
			@RequestParam("contents") String contents) {
		CommentEntity comment = commentService.updateCommentByIdAndUserId(commentId, userId, contents);
		CommonResponse commonResponse = new CommonResponse<CommentEntity>(HttpStatus.OK, "SUCCESS", "댓글 수정 성공",
				comment);
		return ResponseEntity.ok(commonResponse);
	}

	// 댓글 삭제
	@LoginCheck
	@DeleteMapping("/{commentId}")
	public ResponseEntity<CommonResponse> deleteComment(
			@RequestParam(name = "id", required = false) Integer userId,
			@PathVariable("commentId") int commentId) {
		CommentEntity comment = commentService.updateCommentByIdAndUserId(commentId, userId,
				"⚠ 해당 댓글은 작성자에 의해 삭제되었습니다.");
		CommonResponse commonResponse = new CommonResponse<CommentEntity>(HttpStatus.OK, "SUCCESS", "댓글 삭제 성공",
				comment);
		return ResponseEntity.ok(commonResponse);
	}

}
