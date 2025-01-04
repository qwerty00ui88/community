package com.community.dto;

import java.util.List;

import com.community.entity.FileEntity;
import com.community.entity.PostEntity;
import com.community.entity.UserEntity;

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
