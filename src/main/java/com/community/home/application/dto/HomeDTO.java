package com.community.home.application.dto;

import java.util.List;

import com.community.category.domain.Category;
import com.community.post.application.dto.RecentPostsDto;
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
public class HomeDto {
	private List<Category> showOnHomeCategoryList;

	private RecentPostsDto recentPosts;

	private List<Post> mostViewedPosts;
}
