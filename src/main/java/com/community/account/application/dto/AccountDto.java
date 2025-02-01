package com.community.account.application.dto;

import java.time.ZonedDateTime;
import java.util.List;

import com.community.account.domain.AccountStatus;

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
public class AccountDto {
	private int id;
	private String name;
	private String nickname;
	private String password;
	private AccountStatus status;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;
	private List<String> roles;
}
