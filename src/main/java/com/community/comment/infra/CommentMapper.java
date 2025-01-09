package com.community.comment.infra;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.community.comment.application.dto.CommentDTO;

@Mapper
public interface CommentMapper {

    public List<CommentDTO> selectCommentListWithRepliesByPostId(int postId);

}

