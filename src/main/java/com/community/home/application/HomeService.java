package com.community.home.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.community.category.domain.CategoryRepository;
import com.community.category.domain.CategoryStatus;
import com.community.common.PageProcessingException;
import com.community.common.application.PaginationService;
import com.community.home.application.dto.HomeDTO;
import com.community.post.application.PostService;
import com.community.post.application.dto.RecentPostsDTO;
import com.community.post.domain.PostEntity;
import com.community.post.domain.PostStatus;

import lombok.extern.log4j.Log4j2;

@Service
public class HomeService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private PostService postService;

	@Autowired
	private PaginationService paginationService;

	public HomeDTO generateHomeView(Pageable pageable) {
		HomeDTO homeDTO = new HomeDTO();
		try {
			// 홈 표시 카테고리 리스트
			homeDTO.setShowOnHomeCategoryList(
					categoryRepository.findByStatusAndShowOnHome(CategoryStatus.ACTIVE, true));

			// 전체 최신 + 페이징
			Page<PostEntity> postPage = postService.getPostListByStatusNotOrderByCreatedAtDesc(PostStatus.DELETED,
					pageable);
			RecentPostsDTO recentPostsDTO = new RecentPostsDTO(postPage.getContent(),
					paginationService.getPaginationDetails(postPage));
			homeDTO.setRecentPosts(recentPostsDTO);

			// 조회수 순으로 정렬된 게시물
			homeDTO.setMostViewedPosts(postService.getPostListTop10ByStatusNotOrderByViewsDesc(PostStatus.DELETED));

		} catch (Exception e) {
			throw new PageProcessingException("홈 화면 생성 중 오류가 발생했습니다.");
		}

		return homeDTO;
	}
}
