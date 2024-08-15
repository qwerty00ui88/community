package com.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.community.aop.LoginCheck;

@Controller
public class AuthController {

	// 로그인 페이지
	@GetMapping("/login")
	public String loginView(Model model) {
		model.addAttribute("viewName", "include/login");
		return "template/layout";
	}

	// 로그아웃 페이지
	@GetMapping("/signup")
	public String signupView(Model model) {
		model.addAttribute("viewName", "include/signup");
		return "template/layout";
	}

}
