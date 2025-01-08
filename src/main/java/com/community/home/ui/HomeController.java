package com.community.home.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.community.home.command.application.HomeService;
import com.community.home.query.dto.HomeDTO;
import com.community.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private HomeService homeService;

	// 홈 페이지(랜딩 페이지)
	@GetMapping("/" )
	public String homeView(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size, 
			Model model,
			HttpSession session) {
		Pageable pageable = PageRequest.of(page, size);
		HomeDTO homeDTO = homeService.generateHomeView(pageable);

		if(SessionUtil.isLoggedIn(session)) {
			model.addAttribute("nickname", SessionUtil.getLoginNickname(session));
		}
		
		model.addAttribute("homeDTO", homeDTO);
		model.addAttribute("viewName", "include/home");
		return "template/layout";
	}

}
