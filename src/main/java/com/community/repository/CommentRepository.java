package com.community.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.community.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

	public List<CommentEntity> findByUserIdOrderByCreatedAtDesc(int userId);
	
	public Optional<CommentEntity> findByIdAndUserId(int commentId, int userId);
	
}
