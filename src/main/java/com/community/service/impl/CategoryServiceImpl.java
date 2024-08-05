package com.community.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.community.mapper.CategoryMapper;
import com.community.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryMapper categoryMapper;
	
	@Override
	public Map<String, Object> getCategory() {
		return categoryMapper.getCategory();
	}

}
