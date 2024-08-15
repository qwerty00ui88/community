package com.community.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.aop.LoginCheck;
import com.community.dto.RecentPostsDTO;
import com.community.dto.response.CommonResponse;
import com.community.entity.PostEntity;
import com.community.enums.PostStatus;
import com.community.service.PaginationService;
import com.community.service.PostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "게시글 관련 API")
@RestController
@RequestMapping("api/post")
public class PostRestController {

	@Autowired
	private PaginationService paginationService;

	@Autowired
	private PostService postService;

	// 게시글 조회
	@GetMapping
	@Operation(summary = "게시글 조회")
	public ResponseEntity<CommonResponse<RecentPostsDTO>> getPostListByCategoryId(
			@RequestParam("categoryId") int categoryId, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<PostEntity> postPage = null;
		if (categoryId == 0) {
			postPage = postService.getPostListByStatusNotOrderByCreatedAtDesc(PostStatus.DELETED, pageable);
		} else {
			postPage = postService.getPostsByCategoryIdAndStatusNotOrderByCreatedAtDesc(categoryId, PostStatus.DELETED,
					pageable);
		}
		RecentPostsDTO recentPostsDTO = new RecentPostsDTO(postPage.getContent(),
				paginationService.getPaginationDetails(postPage));
		CommonResponse<RecentPostsDTO> commonResponse = CommonResponse.success("게시글 조회 성공", recentPostsDTO);
		return ResponseEntity.ok(commonResponse);
	}

	// 게시글 검색
	@GetMapping("/search")
	@Operation(summary = "게시글 검색")
	public ResponseEntity<CommonResponse<RecentPostsDTO>> getPostSearchResults(@RequestParam("field") String field,
			@RequestParam("keyword") String keyword, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PostEntity> postPage = postService.getPostListByKeyword(field, keyword, pageable);
		RecentPostsDTO recentPostsDTO = new RecentPostsDTO(postPage.getContent(),
				paginationService.getPaginationDetails(postPage));
		CommonResponse<RecentPostsDTO> commonResponse = CommonResponse.success("게시글 조회 성공", recentPostsDTO);
		return ResponseEntity.ok(commonResponse);
	}

	// 게시글 생성
	@LoginCheck
	@PostMapping
	@Operation(summary = "게시글 생성")
	public ResponseEntity<CommonResponse<PostEntity>> createPost(
			@RequestParam(name = "id", required = false) Integer userId, @RequestParam("categoryId") int categoryId,
			@RequestParam("title") String title, @RequestParam("contents") String contents) {
		PostEntity post = postService.createPost(userId, categoryId, title, contents);
		CommonResponse<PostEntity> commonResponse = CommonResponse.success("게시글 생성 성공", post);
		return ResponseEntity.ok(commonResponse);
	}

	// 게시글 수정
	@LoginCheck
	@PutMapping("/{postId}")
	@Operation(summary = "게시글 수정")
	public ResponseEntity<CommonResponse<PostEntity>> updatePosts(
			@RequestParam(name = "id", required = false) Integer userId, @PathVariable("postId") int postId,
			@RequestParam("categoryId") int categoryId, @RequestParam("title") String title,
			@RequestParam("contents") String contents) {
		PostEntity post = postService.updatePostByIdAndUserId(postId, userId, categoryId, title, contents);
		CommonResponse<PostEntity> commonResponse = CommonResponse.success("게시글 수정 성공", post);
		return ResponseEntity.ok(commonResponse);
	}

	// 게시글 삭제
	@LoginCheck
	@DeleteMapping("/{postId}")
	@Operation(summary = "게시글 삭제")
	public ResponseEntity<CommonResponse<Void>> deletePostByIdAndUserId(
			@RequestParam(name = "id", required = false) Integer userId, @PathVariable(name = "postId") int postId) {
		postService.deletePostByIdAndUserId(postId, userId);
		CommonResponse<Void> commonResponse = CommonResponse.success("게시글 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

	// 관리자용 게시글 삭제
	@LoginCheck(type = LoginCheck.UserType.ADMIN)
	@DeleteMapping("/admin/{postId}")
	@Operation(summary = "(관리자용) 게시글 삭제")
	public ResponseEntity<CommonResponse<Void>> deletePostById(
			@RequestParam(name = "id", required = false) Integer userId, @PathVariable(name = "postId") int postId) {
		postService.deletePostById(postId);
		CommonResponse<Void> commonResponse = CommonResponse.success("게시글 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

}
