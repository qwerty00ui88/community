package com.community.user.presentation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.community.common.presentation.dto.CommonResponse;
import com.community.user.application.UserService;
import com.community.user.application.dto.UserDTO;
import com.community.user.application.dto.UserResponseDTO;
import com.community.user.domain.UserEntity;
import com.community.user.domain.UserStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

	@Autowired
	private UserService userService;
	private final PasswordEncoder passwordEncoder;

	// 회원가입***
	@PostMapping("/public/signup")
	@Operation(summary = "회원가입")
	public ResponseEntity<CommonResponse<UserResponseDTO>> signUp(UserDTO accountDto) {
		ModelMapper mapper = new ModelMapper();
		UserEntity account = mapper.map(accountDto, UserEntity.class);
		account.setPassword(passwordEncoder.encode(accountDto.getPassword()));
		userService.createUser(account);
		CommonResponse<UserResponseDTO> commonResponse = CommonResponse.success("회원가입 성공", null);
		return ResponseEntity.ok(commonResponse);
	}

	// 로그아웃***
	@PostMapping("/auth/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "로그아웃")
	public ResponseEntity<CommonResponse<Void>> logout(HttpServletRequest request, HttpServletResponse response) {
		Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext()
				.getAuthentication();
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		CommonResponse<Void> commonResponse = CommonResponse.success("닉네임 중복 확인 완료", null);
		return ResponseEntity.ok(commonResponse);
	}

	// 닉네임 중복 확인***
	@GetMapping("/public/isDuplicatedNickname")
	@Operation(summary = "닉네임 중복 확인")
	public ResponseEntity<CommonResponse<Boolean>> isDuplicatedNickname(@RequestParam("nickname") String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			return ResponseEntity.badRequest()
					.body(CommonResponse.error(HttpStatus.BAD_REQUEST, "FAIL", "닉네임이 유효하지 않습니다."));
		}
		boolean isDuplicated = userService.isDuplicatedNickname(nickname);
		CommonResponse<Boolean> commonResponse = CommonResponse.success("닉네임 중복 확인 완료", isDuplicated);
		return ResponseEntity.ok(commonResponse);
	}

	// 비밀번호 수정***
	@PatchMapping("/auth/updatePassword")
	@PreAuthorize("@accessControl.isOwnerOrAdmin(#userId, authentication)")
	@Operation(summary = "비밀번호 수정")
	public ResponseEntity<CommonResponse<Void>> updateUserPassword(@P("userId") @RequestParam(name = "id") Integer id,
			@RequestParam("beforePassword") String beforePassword, @RequestParam("afterPassword") String afterPassword,
			@RequestParam("afterPasswordCheck") String afterPasswordCheck, Authentication authentication) {
		userService.updatePassword(id, beforePassword, afterPassword, afterPasswordCheck, authentication);
		CommonResponse<Void> commonResponse = CommonResponse.success("비밀번호가 성공적으로 변경되었습니다.", null);
		return ResponseEntity.ok(commonResponse);
	}

	// 회원 상태 수정***
	@PatchMapping("/admin/updateUserStatus")
	@Operation(summary = "(관리자용) 회원 상태 수정")
	public ResponseEntity<CommonResponse<Void>> updateUserStatusByUserId(@RequestParam("id") Integer id,
			@RequestParam("status") UserStatus status) {
		userService.updateStatus(id, status);
		CommonResponse<Void> commonResponse = CommonResponse.success("사용자 상태가 성공적으로 변경되었습니다.", null);
		return ResponseEntity.ok(commonResponse);
	}

	// 회원 탈퇴***
	@DeleteMapping("/auth")
	@PreAuthorize("@accessControl.isOwnerOrAdmin(#userId, authentication)")
	@Operation(summary = "회원 탈퇴")
	public ResponseEntity<CommonResponse<Void>> deleteUserByIdAndPassword(
			@P("userId") @RequestParam(name = "id") Integer id, @RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		userService.deleteUserByIdAndPassword(id, password, authentication);
		if (authentication != null) {
			new SecurityContextLogoutHandler().logout(request, response, authentication);
		}
		CommonResponse<Void> commonResponse = CommonResponse.success("사용자 탈퇴가 성공적으로 처리되었습니다.", null);
		return ResponseEntity.ok(commonResponse);
	}

}
