package com.community.profile.query.dto;

import java.util.List;

import com.community.comment.command.domain.CommentEntity;
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
public class ProfileDTO {
	private UserEntity user;
	private List<PostEntity> postList;
	private List<CommentEntity> commentList;
}
