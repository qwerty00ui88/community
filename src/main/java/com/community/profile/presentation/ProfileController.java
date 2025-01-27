package com.community.profile.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.community.comment.application.CommentService;
import com.community.post.application.PostService;
import com.community.post.domain.PostStatus;
import com.community.profile.application.dto.ProfileDTO;
import com.community.user.application.UserService;

@Controller
public class ProfileController {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@GetMapping("/profile/{userId}")
	@PreAuthorize("@accessControl.isOwnerOrAdmin(#userId, authentication)")
	public String profileView(@P("userId") @PathVariable(name = "userId") Integer userId, Model model) {
		ProfileDTO profile = new ProfileDTO();
		profile.setUser(userService.getUser(userId));
		profile.setPostList(postService.getPostListByUserIdAndStatusNot(userId, PostStatus.DELETED));
		profile.setCommentList(commentService.getCommentListByUserId(userId));
		model.addAttribute("profile", profile);
		model.addAttribute("viewName", "include/profile");
		return "template/layout";
	}

}
