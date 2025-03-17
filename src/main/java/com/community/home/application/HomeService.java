package com.community.home.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.community.category.domain.CategoryRepository;
import com.community.category.domain.CategoryStatus;
import com.community.common.PageProcessingException;
import com.community.common.application.PaginationService;
import com.community.home.application.dto.HomeDto;
import com.community.post.application.PostService;
import com.community.post.application.dto.PostDto;
import com.community.post.application.dto.RecentPostsDto;
import com.community.post.domain.Post;
import com.community.post.domain.PostStatus;

@Service
public class HomeService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private PostService postService;

	@Autowired
	private PaginationService paginationService;

	@Async
	public CompletableFuture<HomeDto> generateHomeView(Pageable pageable) {
		HomeDto homeDto = new HomeDto();
		try {
			CompletableFuture.runAsync(() -> {
				homeDto.setShowOnHomeCategoryList(
						categoryRepository.findByStatusAndShowOnHome(CategoryStatus.ACTIVE, true));
			});
			CompletableFuture<RecentPostsDto> recentPostsFuture = postService
					.getPostListByStatusNotOrderByCreatedAtDesc(PostStatus.DELETED, pageable).thenApply(postPage -> {
						List<PostDto> postList = postPage.getContent().parallelStream().map(PostDto::new)
								.collect(Collectors.toList());
						return new RecentPostsDto(postList, paginationService.getPaginationDetails(postPage));
					});
			CompletableFuture<List<Post>> mostViewedPostsFuture = CompletableFuture
					.supplyAsync(() -> postService.getPostListTop10ByStatusNotOrderByViewsDesc(PostStatus.DELETED));

			return CompletableFuture.allOf(recentPostsFuture, mostViewedPostsFuture).thenApply(v -> {
				try {
					homeDto.setRecentPosts(recentPostsFuture.get());
					homeDto.setMostViewedPosts(mostViewedPostsFuture.get());
				} catch (Exception e) {
					throw new PageProcessingException("홈 화면 생성 중 오류 발생");
				}
				return homeDto;
			});
		} catch (Exception e) {
			throw new PageProcessingException("홈 화면 생성 중 오류 발생");
		}
	}
}
