package com.community.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.community.dto.CategoryPostsDTO;
import com.community.enums.CategoryStatus;

@Mapper
public interface CategoryMapper {

	public List<CategoryPostsDTO> selectCategoriesWithTopNPostsByCategoryIdStatusShowOnHome(
			@Param("limit") Integer limit, @Param("categoryId") Integer categoryId,
			@Param("status") CategoryStatus status, @Param("showOnHome") Boolean showOnHome);

}
