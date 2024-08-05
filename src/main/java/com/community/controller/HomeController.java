package com.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String homeView(Model model) {
		model.addAttribute("viewName", "include/home");
		return "template/layout";
	}
	
}
