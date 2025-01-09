package com.community.post.application.dto;

import java.util.List;

import com.community.common.presentation.dto.PaginationDTO;
import com.community.post.domain.PostEntity;

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
public class RecentPostsDTO {
	private List<PostEntity> postList;
	private PaginationDTO pagination;
}
