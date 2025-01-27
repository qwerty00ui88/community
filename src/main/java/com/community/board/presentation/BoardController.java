package com.community.board.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.board.application.dto.BoardDTO;
import com.community.category.application.CategoryService;
import com.community.category.domain.CategoryStatus;
import com.community.comment.application.CommentService;
import com.community.file.application.FileService;
import com.community.file.domain.FileEntity;
import com.community.post.application.PostService;
import com.community.post.domain.PostEntity;
import com.community.post.domain.PostStatus;
import com.community.user.application.UserService;

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
