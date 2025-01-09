package com.community.user.application.dto;

import java.time.ZonedDateTime;

import com.community.user.domain.UserStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private int id;
	private String name;
	private String nickname;
	private String password;
	private UserStatus status;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
}
