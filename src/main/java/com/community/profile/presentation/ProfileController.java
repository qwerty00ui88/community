package com.community.profile.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.community.account.application.AccountService;
import com.community.comment.application.CommentService;
import com.community.post.application.PostService;
import com.community.post.domain.PostStatus;
import com.community.profile.application.dto.ProfileDto;

@Controller
public class ProfileController {

	@Autowired
	private PostService postService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private CommentService commentService;

	@GetMapping("/profile/{accountId}")
	@PreAuthorize("@accessControl.isOwnerOrAdmin(#accountId, authentication)")
	public String profileView(@P("accountId") @PathVariable(name = "accountId") Integer accountId, Model model) {
		ProfileDto profile = new ProfileDto();
		profile.setAccount(accountService.getAccount(accountId));
		profile.setPostList(postService.getPostListByAccountIdAndStatusNot(accountId, PostStatus.DELETED));
		profile.setCommentList(commentService.getCommentListByAccountId(accountId));
		model.addAttribute("profile", profile);
		model.addAttribute("viewName", "include/profile");
		return "template/layout";
	}

}
