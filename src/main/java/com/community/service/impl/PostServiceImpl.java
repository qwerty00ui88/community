package com.community.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.community.entity.CommentEntity;
import com.community.entity.PostEntity;
import com.community.enums.CommentStatus;
import com.community.enums.PostStatus;
import com.community.repository.PostRepository;
import com.community.service.PostService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PostServiceImpl implements PostService {

	@Autowired
	private PostRepository postRepository;

	// 게시글 생성
	@Override
	public PostEntity createPost(Integer userId, Integer categoryId, String title, String contents) {
		return postRepository.save(
				PostEntity.builder().userId(userId).categoryId(categoryId).title(title).contents(contents).build());
	}

	// 게시글 조회
	public PostEntity getPostById(int postId) {
		return postRepository.findById(postId).orElse(null);
	}

	public List<PostEntity> getPostListByUserIdAndStatusNot(int userId, PostStatus status) {
		return postRepository.findByUserIdAndStatusNot(userId, status);
	}

	public List<PostEntity> getPostListTop10ByStatusNotOrderByViewsDesc(PostStatus status) {
		return postRepository.findTop10ByStatusNotOrderByViewsDesc(status);
	}

	public List<PostEntity> getPostListByStatusNotOrderByCreatedAtDesc(PostStatus status) {
		return postRepository.findByStatusNotOrderByCreatedAtDesc(status);
	}

//	public List<PostEntity> getPostListByCategoryId(int categoryId) {
//	return postRepository.findByCategoryId(categoryId);
//}

//public List<PostEntity> getPostList() {
//return postRepository.findAll();
//}

	// 게시글 수정
	@Override
	public PostEntity updatePostByIdAndUserId(int postId, int userId, Integer categoryId, String title,
			String contents) {
		PostEntity post = postRepository.findByIdAndUserId(postId, userId).orElse(null);
		if (post != null && post.getStatus() != PostStatus.DELETED) {
			post = post.toBuilder().categoryId(categoryId).title(title).contents(contents).status(PostStatus.EDITED)
					.build();
			post = postRepository.save(post);
		}
		return post;
	}

	// 게시글 삭제(userId 체크)
	@Override
	public void deletePostByIdAndUserId(int postId, int userId) {
		PostEntity post = postRepository.findByIdAndUserId(postId, userId).orElse(null);
		if (post != null) {
			post = post.toBuilder().status(PostStatus.DELETED).build();
			postRepository.save(post);
		}
	}

	// 게시글 삭제(userId 체크 X)
	public void deletePostById(int postId) {
		PostEntity post = postRepository.findById(postId).orElse(null);
		if (post != null) {
			post = post.toBuilder().status(PostStatus.DELETED).build();
			postRepository.save(post);
		}
	}

	// 조회수 카운트
	public PostEntity updateViews(Integer postId) {
		PostEntity post = postRepository.findById(postId).orElse(null);
		if (post != null && post.getStatus() != PostStatus.DELETED) {
			post = post.toBuilder().views(post.getViews() + 1).build();
		}
		return postRepository.save(post);
	}

}
