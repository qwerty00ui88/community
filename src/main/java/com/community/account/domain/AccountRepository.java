package com.community.account.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

	public List<Account> findByNicknameContaining(String nickname);

	public Account findByNicknameAndPasswordAndStatusNot(String nickname, String password, AccountStatus status);

	public Account findByNickname(String nickname);

	public Account findByIdAndPassword(Integer id, String password);

	public List<Account> findByIdNot(Integer id);

	public Account getByNickname(String nickname);

}
