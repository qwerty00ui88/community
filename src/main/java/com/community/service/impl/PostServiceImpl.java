package com.community.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.community.dto.PostDTO;
import com.community.entity.PostEntity;
import com.community.exception.PostException;
import com.community.mapper.PostMapper;
import com.community.mapper.UserMapper;
import com.community.repository.PostRepository;
import com.community.service.PostService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PostServiceImpl implements PostService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private PostRepository postRepository;

	// 생성
	@Override
	public PostEntity createPost(Integer userId, Integer categoryId, String title, String contents) {
		return postRepository.save(
				PostEntity.builder().userId(userId).categoryId(categoryId).title(title).contents(contents).build());
	}

	// 조회
	@Override
	public PostEntity getPostById(int postId) {
		return postRepository.findById(postId).orElse(null);
	}

	@Override
	public List<PostEntity> getPostList() {
		return postRepository.findAll();
	}

	@Override
	public List<PostEntity> getPostListByUserId(int userId) {
		return postRepository.findByUserId(userId);
	}

	@Override
	public List<PostEntity> getMostViewedPostList() {
		return postRepository.findTop10ByOrderByViewsDesc();
	}

	@Override
	public List<PostEntity> getPostListByCategoryId(int categoryId) {
		return postRepository.findByCategoryId(categoryId);
	}

	// 수정
	@Override
	public PostEntity updatePostByIdAndUserId(int postId, int userId, Integer categoryId, String title,
			String contents) {
		PostEntity post = postRepository.findByIdAndUserId(postId, userId).orElse(null);
		if (post != null) {
			post = post.toBuilder().categoryId(categoryId).title(title).contents(contents).build();
			post = postRepository.save(post);
		}
		return post;
	}
	
	public PostEntity updateViews(Integer postId) {
	    PostEntity post = postRepository.findById(postId).orElse(null);
	    if(post != null) {
	    	post = post.toBuilder().views(post.getViews() + 1).build();
	    }
	    return postRepository.save(post);
	}


	// 삭제
	@Override
	public void deletePostByIdAndUserId(int postId, int userId) {
		postRepository.deleteByIdAndUserId(postId, userId);
	}
}
