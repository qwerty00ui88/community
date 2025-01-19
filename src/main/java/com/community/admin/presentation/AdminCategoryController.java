package com.community.admin.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.category.application.CategoryService;
import com.community.category.domain.CategoryEntity;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController {

	@Autowired
	private CategoryService categoryService;
	
	// (관리자용) 카테고리 관리 페이지
//	@LoginCheck
	@GetMapping("/manage")
	public String userManagement(Model model) {
		List<CategoryEntity> categoryList = categoryService.getCategoryList();
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("viewName", "include/admin/categoryManage");
		return "template/layout";
	}

}
