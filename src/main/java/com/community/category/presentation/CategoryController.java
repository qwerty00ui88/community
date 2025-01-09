package com.community.category.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.community.category.application.CategoryService;
import com.community.category.domain.CategoryEntity;
import com.community.category.domain.CategoryStatus;
import com.community.common.application.PaginationService;
import com.community.post.application.PostService;
import com.community.post.application.dto.RecentPostsDTO;
import com.community.post.domain.PostEntity;
import com.community.post.domain.PostStatus;

@Controller
@RequestMapping("/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PaginationService paginationService;

	// 전체 카테고리 보기 페이지
	@GetMapping("/all")
	public String all(Model model) {
		model.addAttribute("categoryList", categoryService.getCategoriesWithTopNPostsByCategoryIdStatusShowOnHome(5, null, CategoryStatus.ACTIVE, null));
		model.addAttribute("viewName", "include/categoryList");
		return "template/layout";
	}
	
	// 카테고리별 게시글 보기 페이지
	@GetMapping
	public String detail(
			@RequestParam("categoryId") int categoryId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			Model model) {
		Pageable pageable = PageRequest.of(page, size);
		Page<PostEntity> postPage = postService.getPostsByCategoryIdAndStatusNotOrderByCreatedAtDesc(categoryId, PostStatus.DELETED, pageable);
		RecentPostsDTO recentPostsDTO = new RecentPostsDTO(postPage.getContent(), paginationService.getPaginationDetails(postPage));
		
		CategoryEntity category = categoryService.getCategoryByIdAndStatusNot(categoryId, CategoryStatus.DELETED);
		model.addAttribute("title", category.getName());
		model.addAttribute("categoryId", category.getId());
		model.addAttribute("postList", recentPostsDTO);
		model.addAttribute("viewName", "include/categoryPost");
		return "template/layout";
	}
	
}
