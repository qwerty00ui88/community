package com.community.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.community.entity.PostEntity;

import jakarta.transaction.Transactional;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {

	public Optional<PostEntity> findByIdAndUserId(int id, int userId);

	public List<PostEntity> findByCategoryId(int categoryId);

	@Modifying
	@Transactional
	public void deleteByIdAndUserId(int postId, int userId);

	public List<PostEntity> findTop10ByOrderByViewsDesc();
	
	public List<PostEntity> findByUserId(int userId);

}
