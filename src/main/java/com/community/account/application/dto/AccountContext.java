package com.community.account.application.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.community.account.domain.AccountStatus;

import lombok.Data;

@Data
public class AccountContext implements UserDetails {
	private final AccountDto accountDto;
	private final List<GrantedAuthority> authorities;

	public AccountContext(AccountDto accountDto, List<GrantedAuthority> authorities) {
		this.accountDto = accountDto;
		this.authorities = authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return accountDto.getPassword();
	}

	@Override
	public String getUsername() {
		return accountDto.getNickname();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public Integer getId() {
		return accountDto.getId();
	}

	public AccountStatus getStatus() {
		return accountDto.getStatus();
	}
}
