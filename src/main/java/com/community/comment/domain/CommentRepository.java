package com.community.comment.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

	public List<Comment> findByAccountIdOrderByCreatedAtDesc(int accountId);

	public Optional<Comment> findByIdAndAccountId(int commentId, int accountId);

	@Query("SELECT c.accountId FROM Comment c WHERE c.id = :commentId")
	public Optional<Integer> findAccountIdById(@Param("commentId") int id);

}
