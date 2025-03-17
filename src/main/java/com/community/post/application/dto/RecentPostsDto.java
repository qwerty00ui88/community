package com.community.post.application.dto;

import java.util.List;

import com.community.common.presentation.dto.PaginationDto;

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
	private List<PostDto> postList;
	private PaginationDto pagination;
}
