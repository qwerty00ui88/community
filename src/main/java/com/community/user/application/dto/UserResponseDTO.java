package com.community.user.application.dto;

import java.time.ZonedDateTime;

import com.community.user.domain.UserEntity;
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
public class UserResponseDTO {
	private int id;
	private String name;
	private String nickname;
	private UserStatus status;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	
	public UserResponseDTO(UserEntity user) {
		this.id = user.getId();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.status = user.getStatus();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
	}
}
