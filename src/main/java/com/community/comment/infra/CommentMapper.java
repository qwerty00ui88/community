package com.community.comment.infra;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.community.comment.application.dto.CommentDto;

@Mapper
public interface CommentMapper {

    public List<CommentDto> selectCommentListWithRepliesByPostId(int id);

}

