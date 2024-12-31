package com.community.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.dto.BoardDTO;
import com.community.entity.FileEntity;
import com.community.entity.PostEntity;
import com.community.enums.CategoryStatus;
import com.community.enums.PostStatus;
import com.community.service.CategoryService;
import com.community.service.CommentService;
import com.community.service.FileService;
import com.community.service.PostService;
import com.community.service.UserService;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private PostService postService;

	@Autowired
	private UserService userService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private FileService fileService;
	
	// 게시글 생성 페이지
	@GetMapping("/create")
	public String createPostView(Model model) {
		model.addAttribute("categoryList", categoryService.getCategoryListByStatus(CategoryStatus.ACTIVE));
		model.addAttribute("viewName", "include/createPost");
		return "template/layout";
	}

	// 게시글 조회 페이지
	@GetMapping("/{postId}")
	public String readPostView(@PathVariable("postId") Integer postId, Model model) {
		BoardDTO boardDTO = new BoardDTO();

		PostEntity post = postService.getPostByIdAndStatusNot(postId, PostStatus.DELETED);
		post = postService.updateViews(postId);
		boardDTO.setPost(post);
		boardDTO.setWriter(userService.getUser(post.getUserId()));
		boardDTO.setCommentList(commentService.getCommentListWithRepliesByPostId(postId));

		List<FileEntity> fileList = fileService.getFilesByDomainAndDomainId("post", postId);
		boardDTO.setFileList(fileList);
		
		model.addAttribute("boardDTO", boardDTO);
		model.addAttribute("viewName", "include/readPost");
		return "template/layout";
	}

	// 게시글 수정 페이지
	@GetMapping("/update/{postId}")
	public String updatePostView(@PathVariable("postId") Integer postId, Model model) {
		model.addAttribute("post", postService.getPostByIdAndStatusNot(postId, PostStatus.DELETED));
		model.addAttribute("categoryList", categoryService.getCategoryListByStatus(CategoryStatus.ACTIVE));
		model.addAttribute("fileList", fileService.getFilesByDomainAndDomainId("post", postId));
		model.addAttribute("viewName", "include/updatePost");
		return "template/layout";
	}

}
