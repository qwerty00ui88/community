package com.community.category.query.dto;

import java.util.List;

import com.community.post.command.domain.PostEntity;

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
public class CategoryPostsDTO {
	private Integer categoryId;
	private String categoryName;
	private List<PostEntity> postList;
}
