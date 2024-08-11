package com.community.repository;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.community.entity.PostEntity;
import com.community.enums.PostStatus;

import jakarta.transaction.Transactional;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {

	public Optional<PostEntity> findByIdAndUserId(int id, int userId);

	public List<PostEntity> findByCategoryId(int categoryId);

	@Modifying
	@Transactional
	public void deleteByIdAndUserId(int postId, int userId);

	public List<PostEntity> findTop10ByStatusNotOrderByViewsDesc(PostStatus status);
	
	public List<PostEntity> findByUserIdAndStatusNot(int userId, PostStatus status);
	
	public List<PostEntity> findByStatusNotOrderByCreatedAtDesc(PostStatus status);
	

}
