package com.community.home.query.dto;

import java.util.List;

import com.community.category.command.domain.CategoryEntity;
import com.community.post.command.domain.PostEntity;
import com.community.post.query.dto.RecentPostsDTO;

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
	// 홈 활성화 카테고리
	private List<CategoryEntity> showOnHomeCategoryList;
    
    // 최신 + 페이징
    private RecentPostsDTO recentPosts;
    
    // 조회수 순으로 정렬된 게시물
    private List<PostEntity> mostViewedPosts;
}


