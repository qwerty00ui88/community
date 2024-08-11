package com.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.community.dto.PostDTO;

@Mapper
public interface PostMapper {
	public int createPost(PostDTO postDTO);

	public List<PostDTO> selectMyPostList(int userId);

	public PostDTO selectPostById(int postId);

	public void updateProducts(PostDTO postDTO);

	public void deletePostByUserIdPostId(@Param("userId") int userId, @Param("postId") int postId);

	public List<PostDTO> selectPostList();

	public List<PostDTO> selectMostViewedPostList();
	
	public List<PostDTO> selectPostListByCategory(int categoryId);
}
