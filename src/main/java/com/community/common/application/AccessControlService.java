package com.community.common.application;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.community.account.application.dto.AccountDto;
import com.community.comment.CommentNotFoundException;
import com.community.comment.domain.CommentRepository;
import com.community.post.PostNotFoundException;
import com.community.post.domain.PostRepository;

@Component("accessControl")
public class AccessControlService {

	private final PostRepository postRepository;
	private final CommentRepository commentRepository;

	public AccessControlService(PostRepository postRepository, CommentRepository commentRepository) {
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
	}

	// 게시글 소유권 또는 관리자 권한 확인
	public boolean canAccessPost(int postId, Authentication authentication) {
		Optional<Integer> ownerIdOptional = postRepository.findAccountIdById(postId);
		Integer ownerId = ownerIdOptional.orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다. ID: " + postId));
		return isOwnerOrAdmin(ownerId, authentication);
	}

	// 댓글 소유권 또는 관리자 권한 확인
	public boolean canAccessComment(int commentId, Authentication authentication) {
		Optional<Integer> ownerIdOptional = commentRepository.findAccountIdById(commentId);
		Integer ownerId = ownerIdOptional
				.orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다. ID: " + commentId));
		return isOwnerOrAdmin(ownerId, authentication);
	}

	// 소유권 또는 관리자 권한 확인
	public boolean isOwnerOrAdmin(Integer ownerId, Authentication authentication) {
		Integer currentAccountId = ((AccountDto) authentication.getPrincipal()).getId();
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
		return currentAccountId.equals(ownerId) || isAdmin;
	}
}
