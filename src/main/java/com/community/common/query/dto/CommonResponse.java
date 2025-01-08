package com.community.common.query.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

	private HttpStatus status;
	private String code;
	private String message;
	private T data;

	public static <T> CommonResponse<T> success(String message, T data) {
		return new CommonResponse<>(HttpStatus.OK, "SUCCESS", message, data);
	}

	public static <T> CommonResponse<T> error(HttpStatus status, String code, String message) {
		return new CommonResponse<>(status, code, message, null);
	}

}
