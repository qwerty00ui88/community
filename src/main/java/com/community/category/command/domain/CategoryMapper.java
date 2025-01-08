package com.community.category.command.domain;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.community.category.enums.CategoryStatus;
import com.community.category.query.dto.CategoryPostsDTO;

@Mapper
public interface CategoryMapper {

	public List<CategoryPostsDTO> selectCategoriesWithTopNPostsByCategoryIdStatusShowOnHome(
			@Param("limit") Integer limit, @Param("categoryId") Integer categoryId,
			@Param("status") CategoryStatus status, @Param("showOnHome") Boolean showOnHome);

}
