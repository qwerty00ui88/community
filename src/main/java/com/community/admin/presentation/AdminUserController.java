package com.community.admin.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.user.application.UserService;
import com.community.user.application.dto.UserDTO;

@Controller
@RequestMapping("/admin/user")
public class AdminUserController {

	@Autowired
	private UserService userService;
	
	// (관리자용) 회원 관리 페이지**
	@GetMapping("/manage")
	public String userManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDTO user = (UserDTO) authentication.getPrincipal();
		model.addAttribute("userList", userService.getUsersExcludingId(user.getId()));
		model.addAttribute("viewName", "include/admin/userManage");
		return "template/layout";
	}

}
