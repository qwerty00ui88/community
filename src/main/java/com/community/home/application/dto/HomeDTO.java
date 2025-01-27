package com.community.home.application.dto;

import java.util.List;

import com.community.category.domain.CategoryEntity;
import com.community.post.application.dto.RecentPostsDTO;
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
public class HomeDTO {	
	private List<CategoryEntity> showOnHomeCategoryList;
    
    private RecentPostsDTO recentPosts;
    
    private List<PostEntity> mostViewedPosts;
}


