package com.community.comment.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.community.comment.CommentNotFoundException;
import com.community.comment.InvalidCommentException;
import com.community.comment.application.dto.CommentDTO;
import com.community.comment.domain.CommentEntity;
import com.community.comment.domain.CommentRepository;
import com.community.comment.domain.CommentStatus;
import com.community.comment.infra.CommentMapper;

@Service
public class CommentService {
	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private CommentRepository commentRepository;

	// 댓글 생성
	public CommentEntity createComment(int userId, int postId, Integer parentId, String contents) {
		if (contents == null || contents.trim().isEmpty()) {
			throw new InvalidCommentException("댓글 내용을 입력하세요.");
		}
		return commentRepository.save(
				CommentEntity.builder().userId(userId).postId(postId).parentId(parentId).contents(contents).build());
	}

	// 댓글 조회
	public List<CommentEntity> getCommentListByUserId(int userId) {
		return commentRepository.findByUserIdOrderByCreatedAtDesc(userId);
	}

	public List<CommentDTO> getCommentListWithRepliesByPostId(int postId) {
		return commentMapper.selectCommentListWithRepliesByPostId(postId);
	}

	// 댓글 수정
	public CommentEntity updateCommentById(int commentId, String contents) {
		CommentEntity comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));
		if (comment.getStatus() == CommentStatus.DELETED) {
			throw new InvalidCommentException("삭제된 댓글은 수정할 수 없습니다.");
		}
		comment = comment.toBuilder().contents(contents).status(CommentStatus.EDITED).build();
		return commentRepository.save(comment);
	}

	// 댓글 삭제
	public CommentEntity deleteCommentById(int commentId) {
		CommentEntity comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new CommentNotFoundException("댓글을 찾을 수 없습니다."));
		comment = comment.toBuilder().status(CommentStatus.DELETED).build();
		return commentRepository.save(comment);
	}
}
