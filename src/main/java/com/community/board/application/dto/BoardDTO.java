package com.community.board.application.dto;

import java.util.List;

import com.community.account.domain.Account;
import com.community.comment.application.dto.CommentDto;
import com.community.file.domain.File;
import com.community.post.domain.Post;

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
public class BoardDto {
	private Post post;
	private List<File> fileList;
	private Account writer;
	private List<CommentDto> commentList;
}
