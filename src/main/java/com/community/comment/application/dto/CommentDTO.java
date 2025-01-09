package com.community.comment.application.dto;

import java.time.ZonedDateTime;
import java.util.List;

import com.community.comment.domain.CommentStatus;
import com.community.user.application.dto.UserDTO;

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
public class CommentDTO {
	private int id;
	private int postId;
	private int userId;
	private Integer parentId;
	private String contents;
	private CommentStatus status;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	private UserDTO writer;
	private List<CommentDTO> replyList;
}
