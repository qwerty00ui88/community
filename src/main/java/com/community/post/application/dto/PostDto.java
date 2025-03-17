package com.community.post.application.dto;

import java.time.ZonedDateTime;

import com.community.post.domain.Post;
import com.community.post.domain.PostStatus;

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
public class PostDto {
	private int id;
	private int categoryId;
	private int accountId;
	private String title;
	private String contents;
	private int views;
	private PostStatus status;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	
    public PostDto(Post post) {
        this.id = post.getId();
        this.categoryId = post.getCategoryId();
        this.accountId = post.getAccountId();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.views = post.getViews();
        this.status = post.getStatus();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }
}
