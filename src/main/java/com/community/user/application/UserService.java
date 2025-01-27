package com.community.user.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.community.user.PasswordMismatchException;
import com.community.user.UserNotFoundException;
import com.community.user.domain.Role;
import com.community.user.domain.RoleRepository;
import com.community.user.domain.UserEntity;
import com.community.user.domain.UserRepository;
import com.community.user.domain.UserStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	@Autowired
	private UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	// 회원가입
	@Transactional
	public void createUser(UserEntity account) {
		Role role = roleRepository.findByRoleName("ROLE_USER");
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		account.setUserRoles(roles);
		userRepository.save(account);
	}

	// 회원 조회
	public List<UserEntity> getUsersExcludingId(int id) {
		return userRepository.findByIdNot(id);
	}

	public UserEntity getUser(Integer id) {
		return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
	}

	// 비밀번호 수정
	public boolean updatePassword(int id, String beforePassword, String afterPassword, String afterPasswordCheck,
			Authentication authentication) {
		if (!afterPassword.equals(afterPasswordCheck)) {
			throw new PasswordMismatchException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
		if (!isAdmin && !passwordEncoder.matches(beforePassword, user.getPassword())) {
			throw new UserNotFoundException("현재 비밀번호가 일치하지 않거나 사용자가 존재하지 않습니다.");
		}
		String encodedPassword = passwordEncoder.encode(afterPassword);
		user.setPassword(encodedPassword);
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
	public void deleteUserByIdAndPassword(int id, String password, Authentication authentication) {
		UserEntity user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
		if (!isAdmin && !passwordEncoder.matches(password, user.getPassword())) {
			throw new UserNotFoundException("비밀번호가 일치하지 않거나 사용자가 존재하지 않습니다.");
		}
		user = user.toBuilder().status(UserStatus.DELETED).build();
		userRepository.save(user);
	}

	// 닉네임 중복 확인
	public boolean isDuplicatedNickname(String nickname) {
		return userRepository.findByNickname(nickname) != null;
	}
}
