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
@RequestMapping("/user")
@RestController
public class UserRestController {

	private final UserServiceImpl userService;
	private static final ResponseEntity<LoginResponse> FAIL_RESPONSE = ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(LoginResponse.fail());

	@Autowired
	public UserRestController(UserServiceImpl userService) {
		this.userService = userService;
	}

	@PostMapping("/isDuplicatedNickname")
	public ResponseEntity<Boolean> isDuplicatedId(@RequestParam("nickname") String nickname) {
		boolean isDuplicated = userService.isDuplicatedNickname(nickname);
		return ResponseEntity.ok(isDuplicated);
	}

	@PostMapping("/signup")
	public ResponseEntity<LoginResponse> signUp(@RequestParam("name") String name,
			@RequestParam("nickname") String nickname, @RequestParam("password") String password) {
		UserDTO userDTO = new UserDTO(name, nickname, password, Status.USER, false);
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
		if (userInfo == null || userInfo.getStatus() == Status.DELETED) {
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

	@PostMapping("/logout")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void logout(HttpSession session) {
		SessionUtil.clear(session);
	}

	@LoginCheck
	@GetMapping("/my-info")
	public ResponseEntity<UserDTO> memberInfo(@RequestParam(name = "id", required = false) Integer id,
			HttpSession session) {

		UserDTO memberInfo = userService.getUserInfo(id);
		return ResponseEntity.ok(memberInfo);
	}

	@LoginCheck
	@PatchMapping("/updatePassword")
	public ResponseEntity<LoginResponse> updateUserPassword(@RequestParam(name = "id", required = false) Integer id,
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

	@LoginCheck
	@DeleteMapping("/deleteId")
	public ResponseEntity<LoginResponse> deleteId(@RequestParam(name = "id", required = false) Integer id,
			@RequestParam("password") String password, HttpSession session) {

		try {
			userService.deleteId(id, password);
			SessionUtil.clear(session);
			return ResponseEntity.ok(LoginResponse.success(null));
		} catch (RuntimeException e) {
			log.error("deleteID 실패", e);
			return FAIL_RESPONSE;
		}
	}

}
