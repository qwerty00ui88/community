package com.community.category.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.community.category.application.CategoryService;
import com.community.category.domain.CategoryEntity;
import com.community.category.domain.CategoryStatus;
import com.community.common.presentation.dto.CommonResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "카테고리 관련 API")
@RestController
@RequestMapping("/api/category/admin")
public class CategoryRestController {

	@Autowired
	private CategoryService categoryService;

	// 카테고리 생성***
	@PostMapping
	@Operation(summary = "(관리자용) 카테고리 생성")
	public ResponseEntity<CommonResponse<CategoryEntity>> createCategory(
			@RequestParam(name = "id", required = false) Integer id, @RequestParam("name") String name,
			@RequestParam("status") CategoryStatus status) {
		CategoryEntity category = categoryService.createCategory(name, status);
		CommonResponse<CategoryEntity> commonResponse = CommonResponse.success("카테고리 생성 성공", category);
		return ResponseEntity.ok(commonResponse);
	}

	// 카테고리 수정***
	@PatchMapping("/update")
	@Operation(summary = "(관리자용) 카테고리 수정")
	public ResponseEntity<CommonResponse<CategoryEntity>> updateCategory(
			@RequestParam(name = "id", required = false) Integer userId, @RequestParam("categoryId") Integer categoryId,
			@RequestParam("name") String name, @RequestParam("status") CategoryStatus status) {
		CategoryEntity category = categoryService.updateCategoryById(categoryId, name, status);
		CommonResponse<CategoryEntity> commonResponse = CommonResponse.success("카테고리 수정 성공", category);
		return ResponseEntity.ok(commonResponse);
	}

	// 홈 표시 체크박스***
	@PatchMapping("/showOnHome")
	@Operation(summary = "(관리자용) 카테고리 홈 표시 체크박스 토클")
	public ResponseEntity<CommonResponse<CategoryEntity>> updateCategory(
			@RequestParam(name = "id", required = false) Integer userId, @RequestParam("categoryId") Integer categoryId,
			@RequestParam("showOnHome") boolean showOnHome) {
		CategoryEntity category = categoryService.updateShowOnHome(categoryId, showOnHome);
		CommonResponse<CategoryEntity> commonResponse = CommonResponse.success("홈 표시 상태 변경 성공", category);
		return ResponseEntity.ok(commonResponse);
	}

	// 카테고리 삭제***
	@DeleteMapping("/{categoryId}")
	@Operation(summary = "(관리자용) 카테고리 삭제")
	public ResponseEntity<CommonResponse<Void>> deleteCategoryById(
			@RequestParam(name = "id", required = false) Integer userId,
			@PathVariable(name = "categoryId") int categoryId) {
		categoryService.deleteCategoryById(categoryId);
		CommonResponse<Void> commonResponse = CommonResponse.success("카테고리 삭제 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

}
