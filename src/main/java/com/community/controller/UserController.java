package com.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.community.aop.LoginCheck;

@Controller
public class UserController {

	@GetMapping("/login")
	public String loginView(Model model) {
		model.addAttribute("viewName", "include/login");
		return "template/layout";
	}

	@GetMapping("/signup")
	public String signupView(Model model) {
		model.addAttribute("viewName", "include/signup");
		return "template/layout";
	}

	@LoginCheck
	@GetMapping("/profile")
	public String profileView(Model model) {
		model.addAttribute("viewName", "include/profile");
		return "template/layout";
	}
}
