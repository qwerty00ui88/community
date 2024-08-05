package com.community.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

	public Map<String, Object> getCategory();
	
}
