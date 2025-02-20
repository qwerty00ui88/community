package com.community.post.application.dto;

import java.util.List;

import com.community.common.presentation.dto.PaginationDto;
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
public class RecentPostsDto {
	private List<Post> postList;
	private PaginationDto pagination;
}
