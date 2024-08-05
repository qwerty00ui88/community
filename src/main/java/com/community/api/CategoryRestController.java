package com.community.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.community.service.impl.CategoryServiceImpl;

@RestController
public class CategoryRestController {

	@Autowired
	private CategoryServiceImpl categoryServiceImpl;
	
	@GetMapping("/category")
	public Map<String, Object> getCategory() {
		return categoryServiceImpl.getCategory();
	}
	
}
