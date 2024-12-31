package com.community.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.community.entity.CategoryEntity;
import com.community.entity.PostEntity;
import com.community.entity.UserEntity;
import com.community.enums.CategoryStatus;
import com.community.enums.PostStatus;
import com.community.exception.CategoryNotFoundException;
import com.community.exception.InvalidPostException;
import com.community.exception.PostNotFoundException;
import com.community.exception.UnauthorizedException;
import com.community.repository.CategoryRepository;
import com.community.repository.PostRepository;
import com.community.repository.UserRepository;

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
			List<Integer> userIdList = userList.stream().map(UserEntity::getId) // UserEntity에서 getId 메소드를 사용하여 userId를
																				// 추출
					.collect(Collectors.toList()); // 결과를 List<Integer>로 수집
			return postRepository.findByUserIdInAndStatusNot(userIdList, PostStatus.DELETED, pageable);
		}
		return Page.empty();
	}

	// 게시글 수정
	public PostEntity updatePostByIdAndUserId(int postId, int userId, Integer categoryId, String title, String contents,
			MultipartFile[] newFiles, Integer[] removedFiles) {
		PostEntity post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));
		if (post.getUserId() != userId) {
			throw new UnauthorizedException("이 게시글을 수정할 권한이 없습니다.");
		}
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

	// 게시글 삭제(userId 체크)
	public void deletePostByIdAndUserId(int postId, int userId) {
		PostEntity post = postRepository.findByIdAndUserId(postId, userId)
				.orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

		post = post.toBuilder().status(PostStatus.DELETED).build();
		postRepository.save(post);
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
