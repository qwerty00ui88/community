package com.community.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.community.dto.UserDTO;

@Mapper
public interface UserMapper {
	public UserDTO getUserProfile(@Param("id") int id);

	int insertUserProfile(@Param("id") int id, @Param("password") String password, @Param("name") String name);

	int updateUserProfile(@Param("id") int id, @Param("password") String password, @Param("name") String name);

	int deleteUserProfile(@Param("id") int id);

	public int register(UserDTO userDTO);

	public UserDTO findByIdAndPassword(@Param("id") int id, @Param("password") String password);
	
	public UserDTO findByNicknameAndPassword(@Param("nickname") String nickname, @Param("password") String password);

	int nicknameCheck(String nickname);

	public int updatePassword(UserDTO userDTO);

	public int updateAddress(UserDTO userDTO);
}