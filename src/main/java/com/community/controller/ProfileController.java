package com.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.community.aop.LoginCheck;
import com.community.dto.ProfileDTO;
import com.community.enums.PostStatus;
import com.community.service.CommentService;
import com.community.service.PostService;
import com.community.service.UserService;

@Controller
public class ProfileController {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	// 회원 프로필 페이지
	@LoginCheck
	@GetMapping("/profile")
	public String profileView(@RequestParam(name = "id", required = false) Integer userId, Model model) {
		ProfileDTO profile = new ProfileDTO();
		profile.setUser(userService.getUser(userId));
		profile.setPostList(postService.getPostListByUserIdAndStatusNot(userId, PostStatus.DELETED));
		profile.setCommentList(commentService.getCommentListByUserId(userId));

		model.addAttribute("profile", profile);
		model.addAttribute("viewName", "include/profile");
		return "template/layout";
	}

}
