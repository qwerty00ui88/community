package com.community.board.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.board.query.dto.BoardDTO;
import com.community.category.command.application.CategoryService;
import com.community.category.enums.CategoryStatus;
import com.community.comment.command.application.CommentService;
import com.community.file.command.application.FileService;
import com.community.file.command.domain.FileEntity;
import com.community.post.command.application.PostService;
import com.community.post.command.domain.PostEntity;
import com.community.post.enums.PostStatus;
import com.community.user.command.application.UserService;

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
