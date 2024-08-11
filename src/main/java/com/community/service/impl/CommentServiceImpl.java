package com.community.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.community.dto.CommentDTO;
import com.community.entity.CommentEntity;
import com.community.entity.PostEntity;
import com.community.enums.CommentStatus;
import com.community.exception.PostException;
import com.community.mapper.CommentMapper;
import com.community.repository.CommentRepository;
import com.community.service.CommentService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class CommentServiceImpl implements CommentService {
	@Autowired
	private CommentMapper commentMapper;

	@Autowired
	private CommentRepository commentRepository;

	// 댓글 생성
	public CommentEntity createComment(int userId, int postId, Integer parentId, String contents) {
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
	public CommentEntity updateCommentByIdAndUserId(int commentId, int userId, String contents) {
		CommentEntity comment = commentRepository.findByIdAndUserId(commentId, userId).orElse(null);
		if (comment != null && comment.getStatus() != CommentStatus.DELETED) {
			comment = comment.toBuilder().contents(contents).status(CommentStatus.EDITED).build();
			comment = commentRepository.save(comment);
		}
		return comment;
	}

	// 댓글 삭제(userId 체크)
	public CommentEntity deleteCommentByIdAndUserId(int commentId, int userId) {
		CommentEntity comment = commentRepository.findByIdAndUserId(commentId, userId).orElse(null);
		if (comment != null) {
			comment = comment.toBuilder().status(CommentStatus.DELETED).build();
			comment = commentRepository.save(comment);
		}
		return comment;
	}

	// 댓글 삭제(userId 체크 X)
	public CommentEntity deleteCommentById(int commentId) {
		CommentEntity comment = commentRepository.findById(commentId).orElse(null);
		if (comment != null) {
			comment = comment.toBuilder().status(CommentStatus.DELETED).build();
			comment = commentRepository.save(comment);
		}
		return comment;
	}

}
