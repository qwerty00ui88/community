package com.community.service;

import java.util.List;

import com.community.dto.PostDTO;
import com.community.entity.PostEntity;

public interface PostService {

	// 생성
	public PostEntity createPost(Integer userId, Integer categoryId, String title, String contents);

	// 조회
	public PostEntity getPostById(int postId);

	public List<PostEntity> getPostList();

	public List<PostEntity> getPostListByUserId(int userId);

	public List<PostEntity> getMostViewedPostList();

	public List<PostEntity> getPostListByCategoryId(int categoryId);

	// 수정
	public PostEntity updatePostByIdAndUserId(int postId, int userId, Integer categoryId, String title,
			String contents);
	
	public PostEntity updateViews(Integer postId);

	// 삭제
	public void deletePostByIdAndUserId(int postId, int userId);

}
