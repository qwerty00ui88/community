package com.community.admin.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.community.common.aop.LoginCheck;
import com.community.user.application.UserService;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

	@Autowired
	private UserService userService;
	
	// (관리자용) 회원 관리 페이지
	@LoginCheck(type = LoginCheck.UserType.ADMIN)
	@GetMapping("/manage")
	public String userManagement(@RequestParam(name = "id", required = false) Integer id, Model model) {
		model.addAttribute("userList", userService.getUsersExcludingId(id));
		model.addAttribute("viewName", "include/admin/userManage");
		return "template/layout";
	}

}
