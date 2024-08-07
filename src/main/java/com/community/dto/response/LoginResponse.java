package com.community.dto.response;

import com.community.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class LoginResponse {
	public enum LoginStatus {
		SUCCESS, FAIL, DELETED
	}

	@NonNull
	private LoginStatus status;
	private UserDTO userDTO;

	// 실패 응답은 재사용 가능
	private static final LoginResponse FAIL_RESPONSE = new LoginResponse(LoginStatus.FAIL);

	// 성공 응답 메서드
	public static LoginResponse success(UserDTO userDTO) {
		return new LoginResponse(LoginStatus.SUCCESS, userDTO);
	}

	// 실패 응답 메서드
	public static LoginResponse fail() {
		return FAIL_RESPONSE;
	}
}
