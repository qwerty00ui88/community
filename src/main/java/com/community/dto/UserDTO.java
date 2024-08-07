package com.community.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
	public enum Status {
		USER, ADMIN, DELETED
	}

	private int id;
	private String name;
	private String nickname;
	private String password;
	private Status status;
	private boolean isWithDraw;
	private Date createdAt;
	private Date updatedAt;

	public UserDTO() {
	}

	public UserDTO(String name, String nickname, String password, Status status, boolean isWithDraw) {
		this.name = name;
		this.nickname = nickname;
		this.password = password;
		this.status = status;
		this.isWithDraw = isWithDraw;
	}

	public static boolean hasNullDataBeforeSignup(UserDTO userDTO) {
		return userDTO.getNickname() == null || userDTO.getPassword() == null;
	}
}
