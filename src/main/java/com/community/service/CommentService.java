package com.community.service;

import java.util.List;

import com.community.dto.CommentDTO;
import com.community.entity.CommentEntity;

public interface CommentService {
	public CommentEntity createComment(int userId, int postId, Integer parentId, String contents);

	public List<CommentEntity> getCommentListByUserId(int userId);

	public List<CommentDTO> getCommentListWithRepliesByPostId(int postId);

	public CommentEntity updateCommentByIdAndUserId(int commentId, int userId, String contents);

}
