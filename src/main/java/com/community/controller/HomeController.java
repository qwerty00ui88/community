package com.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.community.dto.HomeDTO;
import com.community.service.impl.PostServiceImpl;

@Controller
public class HomeController {

	@Autowired
	private PostServiceImpl postService;

	@GetMapping("/")
	public String homeView(Model model) {
		HomeDTO homeDTO = new HomeDTO();
		homeDTO.setAllPostList(postService.getPostList());
		homeDTO.setMostViewedPostList(postService.getMostViewedPostList());
		homeDTO.setPostListByCategory1(postService.getPostListByCategoryId(1));
		homeDTO.setPostListByCategory2(postService.getPostListByCategoryId(2));

		model.addAttribute("homeDTO", homeDTO);
		model.addAttribute("viewName", "include/home");
		return "template/layout";
	}

}
