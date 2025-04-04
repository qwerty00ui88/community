package com.community.category.application.dto;

import java.util.List;

import com.community.post.application.dto.PostDto;

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
public class CategoryPostsDto {
	private Integer categoryId;
	private String categoryName;
	private List<PostDto> postList;
}
