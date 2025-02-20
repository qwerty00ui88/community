package com.community.account.application;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.community.account.PasswordMismatchException;
import com.community.account.UserNotFoundException;
import com.community.account.domain.Account;
import com.community.account.domain.AccountRepository;
import com.community.account.domain.AccountStatus;
import com.community.account.domain.Role;
import com.community.account.domain.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	// 회원가입
	@Transactional
	public void createAccount(Account account) {
		Role role = roleRepository.findByRoleName("ROLE_USER");
		Set<Role> roles = new HashSet<>();
		roles.add(role);
		account.setAccountRoles(roles);
		accountRepository.save(account);
	}

	// 회원 조회
	public List<Account> getAccountsExcludingId(int id) {
		return accountRepository.findByIdNot(id);
	}

	public Account getAccount(Integer id) {
		return accountRepository.findById(id).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
	}

	// 비밀번호 수정
	public boolean updatePassword(int id, String beforePassword, String afterPassword, String afterPasswordCheck,
			Authentication authentication) {
		if (!afterPassword.equals(afterPasswordCheck)) {
			throw new PasswordMismatchException("새 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
		}
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
		if (!isAdmin && !passwordEncoder.matches(beforePassword, account.getPassword())) {
			throw new UserNotFoundException("현재 비밀번호가 일치하지 않거나 사용자가 존재하지 않습니다.");
		}
		String encodedPassword = passwordEncoder.encode(afterPassword);
		account.setPassword(encodedPassword);
		accountRepository.save(account);
		return true;
	}

	// 사용자 상태 수정
	public boolean updateStatus(int id, AccountStatus status) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
		account.setStatus(status);
		accountRepository.save(account);
		return true;
	}

	// 회원 탈퇴
	public void deleteAccountByIdAndPassword(int id, String password, Authentication authentication) {
		Account account = accountRepository.findById(id)
				.orElseThrow(() -> new UserNotFoundException("사용자가 존재하지 않습니다."));
		boolean isAdmin = authentication.getAuthorities().stream()
				.anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
		if (!isAdmin && !passwordEncoder.matches(password, account.getPassword())) {
			throw new UserNotFoundException("비밀번호가 일치하지 않거나 사용자가 존재하지 않습니다.");
		}
		account = account.toBuilder().status(AccountStatus.DELETED).build();
		accountRepository.save(account);
	}

	// 닉네임 중복 확인
	public boolean isDuplicatedNickname(String nickname) {
		return accountRepository.findByNickname(nickname) != null;
	}
}
