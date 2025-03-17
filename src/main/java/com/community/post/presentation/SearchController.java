package com.community.post.presentation;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.community.common.application.PaginationService;
import com.community.post.application.PostService;
import com.community.post.application.dto.PostDto;
import com.community.post.application.dto.RecentPostsDto;

@Controller
@RequestMapping("/search")
public class SearchController {

	@Autowired
	private PaginationService paginationService;

	@Autowired
	private PostService postService;

	// 검색 결과 페이지
	@Async
	@GetMapping
	public CompletableFuture<String> getPostSearchResults(@RequestParam("field") String field,
			@RequestParam("keyword") String keyword, @RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model) {
		Pageable pageable = PageRequest.of(page, size);
		return postService.getPostListByKeyword(field, keyword, pageable).thenApply(postPage -> {
			List<PostDto> postList = postPage.getContent().parallelStream().map(PostDto::new)
					.collect(Collectors.toList());
			RecentPostsDto recentPostsDto = new RecentPostsDto(postList,
					paginationService.getPaginationDetails(postPage));
			model.addAttribute("title", "검색 결과: " + keyword);
			model.addAttribute("categoryId", '0');
			model.addAttribute("postList", recentPostsDto);
			model.addAttribute("viewName", "include/categoryPost");
			return "template/layout";
		});
	}

}
