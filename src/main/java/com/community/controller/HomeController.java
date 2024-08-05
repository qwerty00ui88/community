package com.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping("/")
	public String test3(Model model) {
		model.addAttribute("viewName", "home/home");
		return "template/layout";
	}
	
}
