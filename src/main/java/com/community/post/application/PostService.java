package com.community.post.application;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.community.account.domain.Account;
import com.community.account.domain.AccountRepository;
import com.community.category.CategoryNotFoundException;
import com.community.category.domain.Category;
import com.community.category.domain.CategoryRepository;
import com.community.category.domain.CategoryStatus;
import com.community.file.application.FileService;
import com.community.file.application.dto.FileDto;
import com.community.post.InvalidPostException;
import com.community.post.PostNotFoundException;
import com.community.post.domain.Post;
import com.community.post.domain.PostRepository;
import com.community.post.domain.PostStatus;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FileService fileService;

	// 게시글 생성
	@Transactional
	public Post createPost(Integer accountId, Integer categoryId, String title, String contents,
			MultipartFile[] files) {
		if (!categoryRepository.existsById(categoryId)) {
			throw new CategoryNotFoundException("유효하지 않은 카테고리 ID입니다.");
		}
		if (title == null || title.trim().isEmpty()) {
			throw new InvalidPostException("게시글 제목을 입력하세요.");
		}
		if (contents == null || contents.trim().isEmpty()) {
			throw new InvalidPostException("게시글 내용을 입력하세요.");
		}
		// post 테이블 저장
		Post post = postRepository.save(
				Post.builder().accountId(accountId).categoryId(categoryId).title(title).contents(contents).build());
		// 파일 저장
		if (files != null && files.length > 0) {
			fileService.uploadFiles(files, "post", post.getId());
		}
		return post;
	}

	// 게시글 조회
	public Post getPostByIdAndStatusNot(int postId, PostStatus status) {
		return postRepository.findByIdAndStatusNot(postId, PostStatus.DELETED)
				.orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
	}

	public List<Post> getPostListByAccountIdAndStatusNot(int accountId, PostStatus status) {
		return postRepository.findByAccountIdAndStatusNot(accountId, status);
	}

	public List<Post> getPostListTop10ByStatusNotOrderByViewsDesc(PostStatus status) {
		List<Category> categoryList = categoryRepository.findByStatus(CategoryStatus.ACTIVE);
		List<Integer> categoryIdList = categoryList.stream().map(Category::getId).collect(Collectors.toList());
		return postRepository.findTop10ByStatusNotAndCategoryIdInOrderByViewsDesc(status, categoryIdList);
	}

	public CompletableFuture<Page<Post>> getPostListByStatusNotOrderByCreatedAtDesc(PostStatus status,
			Pageable pageable) {
		return CompletableFuture.supplyAsync(() -> {
			List<Category> categoryList = categoryRepository.findByStatus(CategoryStatus.ACTIVE);
			List<Integer> categoryIdList = categoryList.stream().parallel().map(Category::getId)
					.collect(Collectors.toList());
			return postRepository.findByStatusNotAndCategoryIdInOrderByCreatedAtDesc(status, categoryIdList, pageable);
		});
	}

	public CompletableFuture<Page<Post>> getPostsByCategoryIdAndStatusNotOrderByCreatedAtDesc(int categoryId,
			PostStatus status, Pageable pageable) {
		return CompletableFuture.supplyAsync(() -> {
			categoryRepository.findByIdAndStatus(categoryId, CategoryStatus.ACTIVE)
					.orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));
			return postRepository.findByCategoryIdAndStatusNotOrderByCreatedAtDesc(categoryId, status, pageable);
		});
	}

	public CompletableFuture<Page<Post>> getPostListByKeyword(String field, String keyword, Pageable pageable) {
		return CompletableFuture.supplyAsync(() -> {
			if (field.equals("title")) {
				return postRepository.findByTitleContainingAndStatusNot(keyword, PostStatus.DELETED, pageable);
			} else if (field.equals("contents")) {
				return postRepository.findByContentsContainingAndStatusNot(keyword, PostStatus.DELETED, pageable);
			} else if (field.equals("nickname")) {
				List<Account> accountList = accountRepository.findByNicknameContaining(keyword);
				List<Integer> accountIdList = accountList.stream().map(Account::getId).collect(Collectors.toList());
				return postRepository.findByAccountIdInAndStatusNot(accountIdList, PostStatus.DELETED, pageable);
			}
			return Page.empty();
		});
	}

	// 게시글 수정
	public Post updatePostById(int postId, Integer categoryId, String title, String contents, MultipartFile[] newFiles,
			List<Integer> removedFiles) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
		if (post.getStatus() != PostStatus.DELETED) {
			post = post.toBuilder().categoryId(categoryId).title(title).contents(contents).status(PostStatus.EDITED)
					.build();
			post = postRepository.save(post);
		}
		if (newFiles != null && newFiles.length > 0) {
			fileService.uploadFiles(newFiles, "post", post.getId());
		}
		if (removedFiles != null && removedFiles.size() > 0) {
			fileService.deleteFiles(removedFiles);
		}
		return post;
	}

	// 게시글 삭제
	public void deletePostById(int postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
		post = post.toBuilder().status(PostStatus.DELETED).build();
		postRepository.save(post);
	}

	// 조회수 카운트
	public Post updateViews(Integer postId) {
		Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
		if (post.getStatus() != PostStatus.DELETED) {
			post = post.toBuilder().views(post.getViews() + 1).build();
		}
		return postRepository.save(post);
	}

}
