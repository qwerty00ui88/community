package com.community.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.community.entity.UserEntity;
import com.community.enums.UserStatus;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

	public List<UserEntity> findByNicknameContaining(String nickname);
	
	public UserEntity findByNicknameAndPasswordAndStatusNot(String nickname, String password, UserStatus status);

	public UserEntity findByNickname(String nickname);
	
	public UserEntity findByIdAndPassword(Integer id, String password);
	
	public List<UserEntity> findByIdNot(Integer id);
	
	public UserEntity getByNickname(String nickname);
	
}
