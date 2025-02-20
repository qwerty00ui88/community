package com.community.category.infra;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.community.category.application.dto.CategoryPostsDto;
import com.community.category.domain.CategoryStatus;

@Mapper
public interface CategoryMapper {

	public List<CategoryPostsDto> selectCategoriesWithTopNPostsByCategoryIdStatusShowOnHome(
			@Param("limit") Integer limit, @Param("categoryId") Integer categoryId,
			@Param("status") CategoryStatus status, @Param("showOnHome") Boolean showOnHome);

}
