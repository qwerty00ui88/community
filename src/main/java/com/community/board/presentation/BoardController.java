package com.community.board.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.community.account.application.AccountService;
import com.community.board.application.dto.BoardDto;
import com.community.category.application.CategoryService;
import com.community.category.domain.CategoryStatus;
import com.community.comment.application.CommentService;
import com.community.file.application.FileService;
import com.community.file.domain.File;
import com.community.post.application.PostService;
import com.community.post.domain.Post;
import com.community.post.domain.PostStatus;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private PostService postService;

	@Autowired
	private AccountService accountService;

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
		BoardDto boardDto = new BoardDto();

		Post post = postService.getPostByIdAndStatusNot(postId, PostStatus.DELETED);
		post = postService.updateViews(postId);
		boardDto.setPost(post);
		boardDto.setWriter(accountService.getAccount(post.getAccountId()));
		boardDto.setCommentList(commentService.getCommentListWithRepliesByPostId(postId));

		List<File> fileList = fileService.getFilesByDomainAndDomainId("post", postId);
		boardDto.setFileList(fileList);

		model.addAttribute("boardDto", boardDto);
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
