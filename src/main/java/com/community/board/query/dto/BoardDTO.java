package com.community.board.query.dto;

import java.util.List;

import com.community.comment.query.dto.CommentDTO;
import com.community.file.command.domain.FileEntity;
import com.community.post.command.domain.PostEntity;
import com.community.user.command.domain.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
	private PostEntity post;
	private List<FileEntity> fileList;
	private UserEntity writer;
	private List<CommentDTO> commentList;
}
