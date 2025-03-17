package com.community.post.presentation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.community.account.application.dto.AccountDto;
import com.community.common.application.PaginationService;
import com.community.common.presentation.dto.CommonResponse;
import com.community.post.application.PostService;
import com.community.post.application.dto.PostDto;
import com.community.post.application.dto.RecentPostsDto;
import com.community.post.domain.Post;
import com.community.post.domain.PostStatus;

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
	@Async
	@GetMapping("/public")
	@Operation(summary = "게시글 조회")
	public CompletableFuture<ResponseEntity<CommonResponse<RecentPostsDto>>> getPostListByCategoryId(
			@RequestParam("categoryId") int categoryId, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return (categoryId == 0 ? postService.getPostListByStatusNotOrderByCreatedAtDesc(PostStatus.DELETED, pageable)
				: postService.getPostsByCategoryIdAndStatusNotOrderByCreatedAtDesc(categoryId, PostStatus.DELETED,
						pageable))
				.thenApply(postPage -> {
					List<PostDto> postList = postPage.getContent().parallelStream().map(PostDto::new)
							.collect(Collectors.toList());
					RecentPostsDto recentPostsDto = new RecentPostsDto(postList,
							paginationService.getPaginationDetails(postPage));
					CommonResponse<RecentPostsDto> commonResponse = CommonResponse.success("게시글 조회 성공", recentPostsDto);
					return ResponseEntity.ok(commonResponse);
				});
	}

	// 게시글 검색
	@Async
	@GetMapping("/public/search")
	@Operation(summary = "게시글 검색")
	public CompletableFuture<ResponseEntity<CommonResponse<RecentPostsDto>>> getPostSearchResults(
			@RequestParam("field") String field, @RequestParam("keyword") String keyword,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		return postService.getPostListByKeyword(field, keyword, pageable).thenApply(postPage -> {
			List<PostDto> postList = postPage.getContent().parallelStream().map(PostDto::new)
					.collect(Collectors.toList());
			RecentPostsDto recentPostsDto = new RecentPostsDto(postList,
					paginationService.getPaginationDetails(postPage));
			CommonResponse<RecentPostsDto> commonResponse = CommonResponse.success("게시글 조회 성공", recentPostsDto);
			return ResponseEntity.ok(commonResponse);
		});
	}

	// 게시글 생성
	@PostMapping("/auth")
	@Operation(summary = "게시글 생성")
	public ResponseEntity<CommonResponse<Post>> createPost(@RequestParam("categoryId") int categoryId,
			@RequestParam("title") String title, @RequestParam("contents") String contents,
			@RequestParam(name = "files", required = false) MultipartFile[] files,
			@AuthenticationPrincipal AccountDto account) {
		Post post = postService.createPost(account.getId(), categoryId, title, contents, files);
		CommonResponse<Post> commonResponse = CommonResponse.success("게시글 생성 성공", post);
		return ResponseEntity.ok(commonResponse);
	}

	// 게시글 수정
	@PutMapping("/auth/{postId}")
	@PreAuthorize("@accessControl.canAccessPost(#postId, authentication)")
	@Operation(summary = "게시글 수정")
	public ResponseEntity<CommonResponse<Post>> updatePosts(@P("postId") @PathVariable("postId") int postId,
			@RequestParam("categoryId") int categoryId, @RequestParam("title") String title,
			@RequestParam("contents") String contents,
			@RequestParam(name = "newFiles", required = false) MultipartFile[] newFiles,
			@RequestParam(name = "removedFiles", required = false) Integer[] removedFiles) {
		Post post = postService.updatePostById(postId, categoryId, title, contents, newFiles, removedFiles);
		CommonResponse<Post> commonResponse = CommonResponse.success("게시글 수정 성공", post);
		return ResponseEntity.ok(commonResponse);
	}

	// 게시글 삭제
	@DeleteMapping("/auth/{postId}")
	@PreAuthorize("@accessControl.canAccessPost(#postId, authentication)")
	@Operation(summary = "게시글 삭제")
	public ResponseEntity<CommonResponse<Void>> deletePostByIdAndAccountId(
			@P("postId") @PathVariable("postId") int postId) {
		postService.deletePostById(postId);
		CommonResponse<Void> commonResponse = CommonResponse.success("게시글 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

}
