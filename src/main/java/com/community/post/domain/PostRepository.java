package com.community.post.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Integer> {

	public Optional<Post> findByIdAndAccountId(int id, int accountId);

	public Optional<Post> findByIdAndStatusNot(int id, PostStatus status);

	@Query("SELECT p.accountId FROM Post p WHERE p.id = :postId")
	public Optional<Integer> findAccountIdById(@Param("postId") int id);

	public List<Post> findTop10ByStatusNotAndCategoryIdInOrderByViewsDesc(PostStatus status,
			List<Integer> categoryIdList);

	public List<Post> findByAccountIdAndStatusNot(int accountId, PostStatus status);

	public Page<Post> findByStatusNotAndCategoryIdInOrderByCreatedAtDesc(PostStatus status,
			List<Integer> categoryIdList, Pageable pageable);

	public Page<Post> findByCategoryIdAndStatusNotOrderByCreatedAtDesc(int categoryId, PostStatus status,
			Pageable pageable);

	public Page<Post> findByTitleContainingAndStatusNot(String keyword, PostStatus status, Pageable pageable);

	public Page<Post> findByContentsContainingAndStatusNot(String keyword, PostStatus status, Pageable pageable);

	public Page<Post> findByAccountIdInAndStatusNot(List<Integer> accountIdList, PostStatus status, Pageable pageable);

}
