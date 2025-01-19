package com.community.user.presentation;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.community.common.presentation.dto.CommonResponse;
import com.community.common.util.SessionUtil;
import com.community.user.application.UserService;
import com.community.user.application.dto.UserDTO;
import com.community.user.application.dto.UserResponseDTO;
import com.community.user.domain.UserEntity;
import com.community.user.domain.UserStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Tag(name = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserRestController {

	@Autowired
	private UserService userService;
    private final PasswordEncoder passwordEncoder;


	// 회원가입
//	@PostMapping("/public/signup")
//	@Operation(summary = "회원가입")
//	public ResponseEntity<CommonResponse<UserResponseDTO>> signUp(@RequestParam("name") String name,
//			@RequestParam("nickname") String nickname, @RequestParam("password") String password) {
//		UserEntity user = userService.createUser(name, nickname, password);
//		UserResponseDTO userResponseDTO = new UserResponseDTO(user);
//		CommonResponse<UserResponseDTO> commonResponse = CommonResponse.success("회원 생성 성공", userResponseDTO);
//		return ResponseEntity.ok(commonResponse);
//	}

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

	// 로그인
//	@PostMapping("/public/login")
//	@Operation(summary = "로그인")
//	public ResponseEntity<CommonResponse<UserResponseDTO>> login(@RequestParam("nickname") String nickname,
//			@RequestParam("password") String password, HttpSession session) {
//
//		UserEntity user = userService.getUserByNicknameAndPasswordAndStatusNot(nickname, password, UserStatus.DELETED);
//
//		if (user.getStatus() == UserStatus.ADMIN) {
//			SessionUtil.setLoginAdminId(session, user.getId());
//		} else {
//			SessionUtil.setLoginUserId(session, user.getId());
//		}
//		SessionUtil.setLoginNickname(session, user.getNickname());
//
//		UserResponseDTO userResponseDTO = new UserResponseDTO(user);
//		CommonResponse<UserResponseDTO> commonResponse = CommonResponse.success("로그인 성공", userResponseDTO);
//		return ResponseEntity.ok(commonResponse);
//	}

	// 로그아웃
	@PostMapping("/auth/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(summary = "로그아웃")
	public void logout(HttpSession session) {
		SessionUtil.clear(session);
	}

	// 닉네임 중복 확인
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

	// 비밀번호 수정(이전 비밀번호 체크) !!!!!!!사용자면 본인인지 검사, 관리자면 그냥 수정 로직 추가 필요
//	@LoginCheck
	@PatchMapping("/auth/updatePassword")
	@Operation(summary = "비밀번호 수정")
	public ResponseEntity<CommonResponse<Void>> updateUserPassword(
			@RequestParam(name = "id", required = false) Integer id,
			@RequestParam("beforePassword") String beforePassword, @RequestParam("afterPassword") String afterPassword,
			@RequestParam("afterPasswordCheck") String afterPasswordCheck) {
		userService.updatePassword(id, beforePassword, afterPassword, afterPasswordCheck);
		CommonResponse<Void> commonResponse = CommonResponse.success("비밀번호가 성공적으로 변경되었습니다.", null);
		return ResponseEntity.ok(commonResponse);
	}

//	// 관리자용 비밀번호 수정(이전 비밀번호 체크X)
////	@LoginCheck(type = LoginCheck.UserType.ADMIN)
//	@PatchMapping("/admin/updatePassword")
//	@Operation(summary = "(관리자용) 비밀번호 수정")
//	public ResponseEntity<CommonResponse<Void>> updateUserPasswordByUserId(@RequestParam("id") Integer id,
//			@RequestParam("afterPassword") String afterPassword,
//			@RequestParam("afterPasswordCheck") String afterPasswordCheck) {
//		userService.updatePassword(id, afterPassword, afterPasswordCheck);
//		CommonResponse<Void> commonResponse = CommonResponse.success("비밀번호가 성공적으로 변경되었습니다.", null);
//		return ResponseEntity.ok(commonResponse);
//	}

	// 회원 상태 수정
//	@LoginCheck(type = LoginCheck.UserType.ADMIN)
	@PatchMapping("/admin/updateUserStatus")
	@Operation(summary = "(관리자용) 회원 상태 수정")
	public ResponseEntity<CommonResponse<Void>> updateUserStatusByUserId(@RequestParam("id") Integer id,
			@RequestParam("status") UserStatus status) {
		userService.updateStatus(id, status);
		CommonResponse<Void> commonResponse = CommonResponse.success("사용자 상태가 성공적으로 변경되었습니다.", null);
		return ResponseEntity.ok(commonResponse);
	}

	// 회원 탈퇴 !!!!!!!사용자면 본인인지 검사, 관리자면 그냥 삭제 로직 추가 필요
//	@LoginCheck
	@DeleteMapping("/auth")
	@Operation(summary = "회원 탈퇴")
	public ResponseEntity<CommonResponse<Void>> deleteUserByIdAndPassword(
			@RequestParam(name = "id", required = false) Integer id, @RequestParam("password") String password,
			HttpSession session) {
		userService.deleteUserByIdAndPassword(id, password);
		SessionUtil.clear(session);
		CommonResponse<Void> commonResponse = CommonResponse.success("사용자 탈퇴가 성공적으로 처리되었습니다.", null);
		return ResponseEntity.ok(commonResponse);
	}

//	// 관리자용 회원 탈퇴
////	@LoginCheck(type = LoginCheck.UserType.ADMIN)
//	@DeleteMapping("/admin")
//	@Operation(summary = "(관리자용) 회원 탈퇴")
//	public ResponseEntity<CommonResponse<Void>> deleteUserById(@RequestParam(name = "id") Integer id) {
//		userService.deleteUserById(id);
//		CommonResponse<Void> commonResponse = CommonResponse.success("사용자가 성공적으로 삭제되었습니다.", null);
//		return ResponseEntity.ok(commonResponse);
//	}
}
