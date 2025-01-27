package com.community.comment.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

	public List<CommentEntity> findByUserIdOrderByCreatedAtDesc(int userId);

	public Optional<CommentEntity> findByIdAndUserId(int commentId, int userId);

	@Query("SELECT c.userId FROM CommentEntity c WHERE c.id = :commentId")
	public Optional<Integer> findUserIdById(@Param("commentId") int id);

}
