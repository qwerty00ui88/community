package com.community.category.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.community.category.CategoryNotFoundException;
import com.community.category.InvalidCategoryException;
import com.community.category.application.dto.CategoryPostsDto;
import com.community.category.domain.Category;
import com.community.category.domain.CategoryRepository;
import com.community.category.domain.CategoryStatus;
import com.community.category.infra.CategoryMapper;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private CategoryMapper categoryMapper;

	// 카테고리 생성
	public Category createCategory(String name, CategoryStatus status) {
		if (name == null || name.trim().isEmpty()) {
			throw new InvalidCategoryException("카테고리 이름을 입력하세요.");
		}
		return categoryRepository.save(Category.builder().name(name).status(status).build());
	}

	// 카테고리 조회
	public List<Category> getCategoryList() {
		return categoryRepository.findByStatusNot(CategoryStatus.DELETED);
	}

	public List<Category> getCategoryListByStatus(CategoryStatus status) {
		return categoryRepository.findByStatus(status);
	}

	public List<CategoryPostsDto> getCategoriesWithTopNPostsByCategoryIdStatusShowOnHome(Integer limit,
			Integer categoryId, CategoryStatus status, Boolean showOnHome) {
		return categoryMapper.selectCategoriesWithTopNPostsByCategoryIdStatusShowOnHome(limit, categoryId, status,
				showOnHome);
	}

	public Category getCategoryByIdAndStatusNot(int categoryId, CategoryStatus status) {
		return categoryRepository.findByIdAndStatusNot(categoryId, status)
				.orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));
	}

	// 카테고리 수정
	public Category updateCategoryById(Integer categoryId, String name, CategoryStatus status) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

		if (status == CategoryStatus.INACTIVE) {
			category = category.toBuilder().showOnHome(false).build();
		}
		category = category.toBuilder().name(name).status(status).build();
		return categoryRepository.save(category);
	}

	// 홈 표시 체크박스
	public Category updateShowOnHome(Integer categoryId, boolean showOnHome) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));
		category = category.toBuilder().showOnHome(showOnHome).build();
		return categoryRepository.save(category);
	}

	// 카테고리 삭제
	public void deleteCategoryById(int categoryId) {
		Category category = categoryRepository.findById(categoryId)
				.orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));
		category = category.toBuilder().status(CategoryStatus.DELETED).build();
		categoryRepository.save(category);
	}
}
