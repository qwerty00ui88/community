package com.community.post.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.community.category.CategoryNotFoundException;
import com.community.category.domain.CategoryEntity;
import com.community.category.domain.CategoryRepository;
import com.community.category.domain.CategoryStatus;
import com.community.file.application.FileService;
import com.community.post.InvalidPostException;
import com.community.post.PostNotFoundException;
import com.community.post.domain.PostEntity;
import com.community.post.domain.PostRepository;
import com.community.post.domain.PostStatus;
import com.community.user.domain.UserEntity;
import com.community.user.domain.UserRepository;

@Service
public class PostService {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private FileService fileService;

	// 게시글 생성
	public PostEntity createPost(Integer userId, Integer categoryId, String title, String contents,
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
		PostEntity post = postRepository.save(
				PostEntity.builder().userId(userId).categoryId(categoryId).title(title).contents(contents).build());
		if (files != null && files.length > 0) {
			fileService.uploadFiles(files, "post", post.getId());
		}
		return post;
	}

	// 게시글 조회
	public PostEntity getPostByIdAndStatusNot(int postId, PostStatus status) {
		return postRepository.findByIdAndStatusNot(postId, PostStatus.DELETED)
				.orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
	}

	public List<PostEntity> getPostListByUserIdAndStatusNot(int userId, PostStatus status) {
		return postRepository.findByUserIdAndStatusNot(userId, status);
	}

	public List<PostEntity> getPostListTop10ByStatusNotOrderByViewsDesc(PostStatus status) {
		List<CategoryEntity> categoryList = categoryRepository.findByStatus(CategoryStatus.ACTIVE);
		List<Long> categoryIdList = categoryList.stream().map(CategoryEntity::getId).collect(Collectors.toList());
		return postRepository.findTop10ByStatusNotAndCategoryIdInOrderByViewsDesc(status, categoryIdList);
	}

	public Page<PostEntity> getPostListByStatusNotOrderByCreatedAtDesc(PostStatus status, Pageable pageable) {
		List<CategoryEntity> categoryList = categoryRepository.findByStatus(CategoryStatus.ACTIVE);
		List<Long> categoryIdList = categoryList.stream().map(CategoryEntity::getId).collect(Collectors.toList());
		return postRepository.findByStatusNotAndCategoryIdInOrderByCreatedAtDesc(status, categoryIdList, pageable);
	}

	public Page<PostEntity> getPostsByCategoryIdAndStatusNotOrderByCreatedAtDesc(int categoryId, PostStatus status,
			Pageable pageable) {
		categoryRepository.findByIdAndStatus(categoryId, CategoryStatus.ACTIVE)
				.orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));
		return postRepository.findByCategoryIdAndStatusNotOrderByCreatedAtDesc(categoryId, status, pageable);
	}

	public Page<PostEntity> getPostListByKeyword(String field, String keyword, Pageable pageable) {
		if (field.equals("title")) {
			return postRepository.findByTitleContainingAndStatusNot(keyword, PostStatus.DELETED, pageable);
		} else if (field.equals("contents")) {
			return postRepository.findByContentsContainingAndStatusNot(keyword, PostStatus.DELETED, pageable);
		} else if (field.equals("nickname")) {
			List<UserEntity> userList = userRepository.findByNicknameContaining(keyword);
			List<Integer> userIdList = userList.stream().map(UserEntity::getId).collect(Collectors.toList());
			return postRepository.findByUserIdInAndStatusNot(userIdList, PostStatus.DELETED, pageable);
		}
		return Page.empty();
	}

	// 게시글 수정
	public PostEntity updatePostById(int postId, Integer categoryId, String title, String contents,
			MultipartFile[] newFiles, Integer[] removedFiles) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
		if (post.getStatus() != PostStatus.DELETED) {
			post = post.toBuilder().categoryId(categoryId).title(title).contents(contents).status(PostStatus.EDITED)
					.build();
			post = postRepository.save(post);
		}
		if (newFiles != null && newFiles.length > 0) {
			fileService.uploadFiles(newFiles, "post", post.getId());
		}
		if (removedFiles != null && removedFiles.length > 0) {
			fileService.deleteFiles(removedFiles);
		}
		return post;
	}

	// 게시글 삭제(userId 체크 X)
	public void deletePostById(int postId) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
		post = post.toBuilder().status(PostStatus.DELETED).build();
		postRepository.save(post);
	}

	// 조회수 카운트
	public PostEntity updateViews(Integer postId) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
		if (post.getStatus() != PostStatus.DELETED) {
			post = post.toBuilder().views(post.getViews() + 1).build();
		}
		return postRepository.save(post);
	}

}
