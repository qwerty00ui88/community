package com.community.account.application.dto;

import java.time.ZonedDateTime;

import com.community.account.domain.Account;
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
public class AccountResponseDto {
	private int id;
	private String name;
	private String nickname;
	private AccountStatus status;
	private ZonedDateTime createdAt;
	private ZonedDateTime updatedAt;

	public AccountResponseDto(Account account) {
		this.id = account.getId();
		this.name = account.getName();
		this.nickname = account.getNickname();
		this.status = account.getStatus();
		this.createdAt = account.getCreatedAt();
		this.updatedAt = account.getUpdatedAt();
	}
}
