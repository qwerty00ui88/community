package com.community.comment.command.domain;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.community.comment.query.dto.CommentDTO;

@Mapper
public interface CommentMapper {

    public List<CommentDTO> selectCommentListWithRepliesByPostId(int postId);

}

