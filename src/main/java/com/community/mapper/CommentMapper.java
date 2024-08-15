package com.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.community.dto.CommentDTO;

@Mapper
public interface CommentMapper {

    public List<CommentDTO> selectCommentListWithRepliesByPostId(int postId);

}

