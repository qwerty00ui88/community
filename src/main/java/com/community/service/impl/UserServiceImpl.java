package com.community.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.community.dto.UserDTO;
import com.community.exception.DuplicateIdException;
import com.community.mapper.UserMapper;
import com.community.service.UserService;
import com.community.utils.SHA256Util;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper UserMapper;

	public UserServiceImpl(UserMapper UserMapper) {
		this.UserMapper = UserMapper;
	}

	@Override
	public UserDTO getUserInfo(int id) {
		return UserMapper.getUserProfile(id);
	}

	@Override
	public void register(UserDTO userDTO) {
		boolean duplIdResult = isDuplicatedNickname(userDTO.getNickname());
		if (duplIdResult) {
			throw new DuplicateIdException("중복된 아이디입니다.");
		}
		userDTO.setPassword(SHA256Util.encryptSHA256(userDTO.getPassword()));
		int insertCount = UserMapper.register(userDTO);

		if (insertCount != 1) {
			log.error("insertMember ERROR! {}", userDTO);
			throw new RuntimeException("insertUser ERROR! 회원가입 메서드를 확인해주세요\n" + "Params : " + userDTO);
		}
	}

	@Override
	public UserDTO login(String nickname, String password) {
		String cryptoPassword = SHA256Util.encryptSHA256(password);
		UserDTO memberInfo = UserMapper.findByNicknameAndPassword(nickname, cryptoPassword);
		return memberInfo;
	}

	@Override
	public boolean isDuplicatedNickname(String nickname) {
		return UserMapper.nicknameCheck(nickname) == 1;
	}

	@Override
	public void updatePassword(int id, String beforePassword, String afterPassword) {
		String cryptoPassword = SHA256Util.encryptSHA256(beforePassword);
		UserDTO memberInfo = UserMapper.findByIdAndPassword(id, cryptoPassword);

		if (memberInfo != null) {
			memberInfo.setPassword(SHA256Util.encryptSHA256(afterPassword));
			int insertCount = UserMapper.updatePassword(memberInfo);
		} else {
			log.error("updatePasswrod ERROR! {}", memberInfo);
			throw new IllegalArgumentException(
					"updatePasswrod ERROR! 비밀번호 변경 메서드를 확인해주세요\n" + "Params : " + memberInfo);
		}
	}

	@Override
	public void deleteId(int id, String password) {
		String cryptoPassword = SHA256Util.encryptSHA256(password);
		UserDTO memberInfo = UserMapper.findByIdAndPassword(id, cryptoPassword);

		if (memberInfo != null) {
			UserMapper.deleteUserProfile(memberInfo.getId());
		} else {
			log.error("deleteId ERROR! {}", memberInfo);
			throw new RuntimeException("deleteId ERROR! id 삭제 메서드를 확인해주세요\n" + "Params : " + memberInfo);
		}
	}

}
