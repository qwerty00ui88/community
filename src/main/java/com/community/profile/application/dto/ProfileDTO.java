package com.community.profile.application.dto;

import java.util.List;

import com.community.account.domain.Account;
import com.community.comment.domain.Comment;
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
public class ProfileDto {
	private Account account;
	private List<Post> postList;
	private List<Comment> commentList;
}
