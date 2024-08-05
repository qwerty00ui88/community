package com.community.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class TestController {
	
	@GetMapping("/test1")
	@ResponseBody
	public String test1() {
		return "Hello World";
	}
	
	@GetMapping("/test2")
	@ResponseBody
	public Map<String, Object> test2() {
		Map<String, Object> testMap = new HashMap<>();
		testMap.put("hello", "world");
		return testMap;
	}
	
	@GetMapping("/test3")
	public String test3(Model model) {
		model.addAttribute("viewName", "test/test");
		return "template/layout";
	}
	
}
