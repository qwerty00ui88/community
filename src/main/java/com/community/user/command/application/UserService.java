package com.community.user.command.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.community.user.PasswordMismatchException;
import com.community.user.UserAlreadyExistsException;
import com.community.user.UserNotFoundException;
import com.community.user.command.domain.UserEntity;
import com.community.user.command.domain.UserRepository;
import com.community.user.enums.UserStatus;
import com.community.utils.SHA256Util;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	// 회원 생성
	public UserEntity createUser(String name, String nickname, String password) {
		UserEntity user = userRepository.getByNickname(nickname);
		if (user != null) {
			throw new UserAlreadyExistsException("이미 존재하는 닉네임입니다.");
		}
		String cryptoPassword = SHA256Util.encryptSHA256(password);
		return userRepository.save(UserEntity.builder().name(name).nickname(nickname).password(cryptoPassword).build());
	}

	// 회원 조회
	public List<UserEntity> getUsersExcludingId(int id) {
		return userRepository.findByIdNot(id);
	}

	public UserEntity getUser(Integer id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
	}

	public UserEntity getUserByNicknameAndPasswordAndStatusNot(String nickname, String password, UserStatus status) {
		String cryptoPassword = SHA256Util.encryptSHA256(password);
		UserEntity user = userRepository.findByNicknameAndPasswordAndStatusNot(nickname, cryptoPassword, status);
		if (user == null) {
			throw new UserNotFoundException("닉네임 또는 비밀번호가 일치하지 않거나, 상태가 유효하지 않습니다.");
		}
		return user;
	}

	// 비밀번호 수정(이전 비밀번호 체크)
	public boolean updatePassword(int id, String beforePassword, String afterPassword, String afterPasswordCheck) {

		if (!afterPassword.equals(afterPasswordCheck)) {
			throw new PasswordMismatchException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}

		UserEntity user = userRepository.findByIdAndPassword(id, SHA256Util.encryptSHA256(beforePassword));
		if (user == null) {
			throw new UserNotFoundException("현재 비밀번호가 일치하지 않거나 사용자가 존재하지 않습니다.");
		}

		user.setPassword(SHA256Util.encryptSHA256(afterPassword));
		userRepository.save(user);
		return true;
	}

	// 관리자용 비밀번호 수정(이전 비밀번호 체크X)
	public boolean updatePassword(int id, String afterPassword, String afterPasswordCheck) {
		if (!afterPassword.equals(afterPasswordCheck)) {
			throw new PasswordMismatchException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}

		UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

		user.setPassword(SHA256Util.encryptSHA256(afterPassword));
		userRepository.save(user);
		return true;
	}

	// 사용자 상태 수정
	public boolean updateStatus(int id, UserStatus status) {
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));

		user.setStatus(status);
		userRepository.save(user);
		return true;
	}

	// 회원 탈퇴
	public void deleteUserByIdAndPassword(int id, String password) {
		String cryptoPassword = SHA256Util.encryptSHA256(password);
		UserEntity user = userRepository.findByIdAndPassword(id, cryptoPassword);
		if (user != null) {
			user = user.toBuilder().status(UserStatus.DELETED).build();
			userRepository.save(user);
		} else {
			throw new UserNotFoundException("비밀번호가 일치하지 않거나 사용자가 존재하지 않습니다.");
		}
	}

	// 관리자용 회원 탈퇴
	public void deleteUserById(int id) {
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
		user = user.toBuilder().status(UserStatus.DELETED).build();
		userRepository.save(user);
	}

	// 닉네임 중복 확인
	public boolean isDuplicatedNickname(String nickname) {
		return userRepository.findByNickname(nickname) != null;
	}
}
