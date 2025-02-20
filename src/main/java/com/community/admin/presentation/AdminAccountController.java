package com.community.admin.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.account.application.AccountService;
import com.community.account.application.dto.AccountDto;

@Controller
@RequestMapping("/admin/account")
public class AdminAccountController {

	@Autowired
	private AccountService accountService;

	// (관리자용) 회원 관리 페이지
	@GetMapping("/manage")
	public String accountManagement(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AccountDto account = (AccountDto) authentication.getPrincipal();
		model.addAttribute("accountList", accountService.getAccountsExcludingId(account.getId()));
		model.addAttribute("viewName", "include/admin/accountManage");
		return "template/layout";
	}

}
