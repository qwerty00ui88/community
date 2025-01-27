package com.community.post.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {

	public Optional<PostEntity> findByIdAndUserId(int id, int userId);

	public Optional<PostEntity> findByIdAndStatusNot(int id, PostStatus status);

	@Query("SELECT p.userId FROM PostEntity p WHERE p.id = :postId")
	public Optional<Integer> findUserIdById(@Param("postId") int id);
	
	public List<PostEntity> findTop10ByStatusNotAndCategoryIdInOrderByViewsDesc(PostStatus status,
			List<Long> categoryIdList);

	public List<PostEntity> findByUserIdAndStatusNot(int userId, PostStatus status);

	public Page<PostEntity> findByStatusNotAndCategoryIdInOrderByCreatedAtDesc(PostStatus status,
			List<Long> categoryIdList, Pageable pageable);

	public Page<PostEntity> findByCategoryIdAndStatusNotOrderByCreatedAtDesc(int categoryId, PostStatus status,
			Pageable pageable);

	public Page<PostEntity> findByTitleContainingAndStatusNot(String keyword, PostStatus status, Pageable pageable);

	public Page<PostEntity> findByContentsContainingAndStatusNot(String keyword, PostStatus status, Pageable pageable);

	public Page<PostEntity> findByUserIdInAndStatusNot(List<Integer> userIdList, PostStatus status, Pageable pageable);

}
