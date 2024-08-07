package com.community.service;

import com.community.dto.UserDTO;

public interface UserService {
	void register(UserDTO userProfile);

	UserDTO login(String nickname, String password);

	boolean isDuplicatedNickname(String nickname);

	UserDTO getUserInfo(Integer id);

	void updatePassword(int id, String beforePassword, String afterPassword);

	void deleteId(int id, String password);
}
