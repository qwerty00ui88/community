package com.community.comment.application.dto;

import java.time.ZonedDateTime;
import java.util.List;

import com.community.account.application.dto.AccountDto;
import com.community.comment.domain.CommentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class CommentDto {
	private int id;
	private int postId;
	private int accountId;
	private Integer parentId;
	private String contents;
	private CommentStatus status;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	private AccountDto writer;
	private List<CommentDto> replyList;
}
