package com.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/post")
@Controller
public class PostController {

	@GetMapping("/1")
	public String getPost(Model model) {
		model.addAttribute("viewName", "include/viewPost");
		return "template/layout";
	}

	@GetMapping("/create")
	public String createPost(Model model) {
		model.addAttribute("viewName", "include/createPost");
		return "template/layout";
	}

}
