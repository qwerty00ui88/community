package com.community.home.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.community.home.application.HomeService;
import com.community.home.application.dto.HomeDto;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private HomeService homeService;

	// 홈 페이지(랜딩 페이지)
	@GetMapping("/")
	public String homeView(@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, Model model, HttpSession session) {
		Pageable pageable = PageRequest.of(page, size);
		try {
			HomeDto homeDto = homeService.generateHomeView(pageable).get();
			model.addAttribute("homeDto", homeDto);
			model.addAttribute("viewName", "include/home");
		} catch (Exception e) {
			throw new RuntimeException("홈 화면 생성 중 오류 발생", e);
		}
		return "template/layout";
	}
}
