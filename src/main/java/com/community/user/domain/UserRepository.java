package com.community.user.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	public List<UserEntity> findByNicknameContaining(String nickname);
	
	public UserEntity findByNicknameAndPasswordAndStatusNot(String nickname, String password, UserStatus status);

	public UserEntity findByNickname(String nickname);
	
	public UserEntity findByIdAndPassword(Integer id, String password);
	
	public List<UserEntity> findByIdNot(Integer id);
	
	public UserEntity getByNickname(String nickname);
	
}
