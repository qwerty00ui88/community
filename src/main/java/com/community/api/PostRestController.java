package com.community.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.aop.LoginCheck;
import com.community.dto.PostDTO;
import com.community.dto.response.CommonResponse;
import com.community.entity.PostEntity;
import com.community.service.impl.PostServiceImpl;
import com.community.service.impl.UserServiceImpl;

@RequestMapping("api/post")
@RestController
public class PostRestController {

	private final PostServiceImpl postService;
	private final UserServiceImpl userService;

	public PostRestController(PostServiceImpl postService, UserServiceImpl userService) {
		this.postService = postService;
		this.userService = userService;
	}

	// 게시글 생성
	@LoginCheck(type = LoginCheck.UserType.USER)
	@PostMapping("/create")
	public ResponseEntity<CommonResponse> createPost(@RequestParam(name = "id", required = false) Integer userId,
			@RequestParam("categoryId") int categoryId, @RequestParam("title") String title,
			@RequestParam("contents") String contents) {
		PostEntity post = postService.createPost(userId, categoryId, title, contents);
		CommonResponse commonResponse = new CommonResponse<PostEntity>(HttpStatus.OK, "SUCCESS", "게시글 생성 성공", post);
		return ResponseEntity.ok(commonResponse);
	}

	// 게시글 수정
	@LoginCheck(type = LoginCheck.UserType.USER)
	@PutMapping("/{postId}")
	public ResponseEntity<CommonResponse> updatePosts(@RequestParam(name = "id", required = false) Integer userId,
			@PathVariable("postId") int postId, @RequestParam("categoryId") int categoryId,
			@RequestParam("title") String title, @RequestParam("contents") String contents) {
		PostEntity post = postService.updatePostByIdAndUserId(postId, userId, categoryId, title, contents);
		CommonResponse commonResponse = new CommonResponse<PostEntity>(HttpStatus.OK, "SUCCESS", "게시글 수정 성공", post);
		return ResponseEntity.ok(commonResponse);
	}

	// 게시글 삭제
	@LoginCheck(type = LoginCheck.UserType.USER)
	@DeleteMapping("/{postId}")
	public ResponseEntity<CommonResponse> deletePostByUserIdPostId(
			@RequestParam(name = "id", required = false) Integer userId, @PathVariable(name = "postId") int postId) {
		postService.deletePostByIdAndUserId(postId, userId);
		CommonResponse commonResponse = new CommonResponse<>(HttpStatus.OK, "SUCCESS", "게시글 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

}
