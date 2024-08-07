package com.community.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.community.aop.LoginCheck;
import com.community.dto.UserDTO;
import com.community.dto.UserDTO.Status;
import com.community.dto.response.LoginResponse;
import com.community.service.impl.UserServiceImpl;
import com.community.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/user")
public class UserRestController {

	private final UserServiceImpl userService;
	private static final ResponseEntity<LoginResponse> FAIL_RESPONSE = ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(LoginResponse.fail());

	@Autowired
	public UserRestController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@PostMapping("/signup")
	public ResponseEntity<LoginResponse> signUp(@RequestParam("name") String name,
			@RequestParam("nickname") String nickname, @RequestParam("password") String password) {
		UserDTO userDTO = new UserDTO(name, nickname, password, Status.DEFAULT, false);
		if (UserDTO.hasNullDataBeforeSignup(userDTO)) {
			LoginResponse response = new LoginResponse(LoginResponse.LoginStatus.FAIL, null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		userService.register(userDTO);
		LoginResponse response = new LoginResponse(LoginResponse.LoginStatus.SUCCESS, userDTO);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestParam("nickname") String nickname,
			@RequestParam("password") String password, HttpSession session) {
		UserDTO userInfo = userService.login(nickname, password);

		// 실패 시
		if (userInfo == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(LoginResponse.fail());
		}

		// 성공 시
		int id = userInfo.getId();

		if (userInfo.getStatus() == Status.ADMIN) {
			SessionUtil.setLoginAdminId(session, id);
		} else {
			SessionUtil.setLoginMemberId(session, id);
		}

		LoginResponse loginResponse = LoginResponse.success(userInfo);
		return ResponseEntity.ok(loginResponse);
	}

	@GetMapping("/my-info")
	public ResponseEntity<UserDTO> memberInfo(HttpSession session) {
		Integer id = SessionUtil.getLoginId(session);

		if (id == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		UserDTO memberInfo = userService.getUserInfo(id);
		return ResponseEntity.ok(memberInfo);
	}

	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout(HttpSession session) {
		SessionUtil.clear(session);
	}

	@PatchMapping("/password")
	@LoginCheck(type = LoginCheck.UserType.USER)
	public ResponseEntity<LoginResponse> updateUserPassword(int id,
			@RequestParam("beforePassword") String beforePassword,
			@RequestParam("afterPassword") String afterPassword) {
		try {
			userService.updatePassword(id, beforePassword, afterPassword);
			return ResponseEntity.ok(LoginResponse.success(null));
		} catch (IllegalArgumentException e) {
			log.error("updatePassword 실패", e);
			return FAIL_RESPONSE;
		}
	}

	@DeleteMapping("/deleteId")
	public ResponseEntity<LoginResponse> deleteId(@RequestParam("password") String password, HttpSession session) {
		Integer id = SessionUtil.getLoginMemberId(session);

		try {
			userService.deleteId(id, password);
			return ResponseEntity.ok(LoginResponse.success(null));
		} catch (RuntimeException e) {
			log.error("deleteID 실패", e);
			return FAIL_RESPONSE;
		}
	}

	@PostMapping("/isDuplicatedNickname")
	public ResponseEntity<Boolean> isDuplicatedId(@RequestParam("nickname") String nickname) {
		boolean isDuplicated = userService.isDuplicatedNickname(nickname);
		return ResponseEntity.ok(isDuplicated);
	}
}
