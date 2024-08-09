package com.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.community.aop.LoginCheck;
import com.community.dto.ProfileDTO;
import com.community.service.impl.CommentServiceImpl;
import com.community.service.impl.PostServiceImpl;
import com.community.service.impl.UserServiceImpl;

@Controller
public class ProfileController {

	@Autowired
	private PostServiceImpl postService;
	
	@Autowired
	private UserServiceImpl userService;
	
	@Autowired
	private CommentServiceImpl commentService;

	@LoginCheck
	@GetMapping("/profile")
	public String profileView(@RequestParam(name = "id", required = false) Integer userId, Model model) {
		ProfileDTO profile = new ProfileDTO();
		// 사용자 정보
		profile.setUser(userService.getUserInfo(userId));
		
		// 게시글 정보
		profile.setPostList(postService.getPostListByUserId(userId));
		
		// 댓글 정보
		profile.setCommentList(commentService.getCommentListByUserId(userId));
		
		model.addAttribute("profile", profile);
		model.addAttribute("viewName", "include/profile");
		return "template/layout";
	}
}
