package com.community.home.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.community.category.domain.CategoryRepository;
import com.community.category.domain.CategoryStatus;
import com.community.common.PageProcessingException;
import com.community.common.application.PaginationService;
import com.community.home.application.dto.HomeDto;
import com.community.post.application.PostService;
import com.community.post.application.dto.RecentPostsDto;
import com.community.post.domain.Post;
import com.community.post.domain.PostStatus;

@Service
public class HomeService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private PostService postService;

	@Autowired
	private PaginationService paginationService;

	public HomeDto generateHomeView(Pageable pageable) {
		HomeDto homeDto = new HomeDto();
		try {
			// 홈 표시 카테고리 리스트
			homeDto.setShowOnHomeCategoryList(
					categoryRepository.findByStatusAndShowOnHome(CategoryStatus.ACTIVE, true));

			// 전체 최신 + 페이징
			Page<Post> postPage = postService.getPostListByStatusNotOrderByCreatedAtDesc(PostStatus.DELETED, pageable);
			RecentPostsDto recentPostsDto = new RecentPostsDto(postPage.getContent(),
					paginationService.getPaginationDetails(postPage));
			homeDto.setRecentPosts(recentPostsDto);

			// 조회수 순으로 정렬된 게시물
			homeDto.setMostViewedPosts(postService.getPostListTop10ByStatusNotOrderByViewsDesc(PostStatus.DELETED));

		} catch (Exception e) {
			throw new PageProcessingException("홈 화면 생성 중 오류가 발생했습니다.");
		}

		return homeDto;
	}
}
