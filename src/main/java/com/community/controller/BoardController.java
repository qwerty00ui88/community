package com.community.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.dto.BoardDTO;
import com.community.entity.PostEntity;
import com.community.service.impl.CommentServiceImpl;
import com.community.service.impl.PostServiceImpl;
import com.community.service.impl.UserServiceImpl;

@RequestMapping("/board")
@Controller
public class BoardController {

	@Autowired
	private PostServiceImpl postService;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private CommentServiceImpl commentService;

	// 게시글 생성 페이지
	@GetMapping("/create")
	public String createPostView(Model model) {
		model.addAttribute("viewName", "include/createPost");
		return "template/layout";
	}

	// 게시글 조회 페이지
	@GetMapping("/{postId}")
	public String readPostView(@PathVariable("postId") Integer postId, Model model) {
		BoardDTO boardDTO = new BoardDTO();
		// 게시글
		PostEntity post = postService.updateViews(postId);
		boardDTO.setPost(post);

		// 작성자
		boardDTO.setWriter(userService.getUserInfo(post.getUserId()));

		// 댓글

		model.addAttribute("boardDTO", boardDTO);
		model.addAttribute("viewName", "include/readPost");
		return "template/layout";
	}

	// 게시글 수정 페이지
	@GetMapping("/update/{postId}")
	public String updatePostView(@PathVariable("postId") Integer postId, Model model) {
		model.addAttribute("post", postService.getPostById(postId));
		model.addAttribute("viewName", "include/updatePost");
		return "template/layout";
	}

}
