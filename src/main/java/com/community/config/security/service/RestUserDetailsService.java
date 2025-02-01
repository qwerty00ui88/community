package com.community.config.security.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.community.account.application.dto.AccountContext;
import com.community.account.application.dto.AccountDto;
import com.community.account.domain.Account;
import com.community.account.domain.AccountRepository;
import com.community.account.domain.Role;

import lombok.RequiredArgsConstructor;

@Service("userDetailsService")
@RequiredArgsConstructor
public class RestUserDetailsService implements UserDetailsService {

	private final AccountRepository accountRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Account account = accountRepository.findByNickname(username);
		if (account == null) {
			throw new UsernameNotFoundException("No user found with username: " + username);
		}
		List<GrantedAuthority> authorities = account.getAccountRoles().stream().map(Role::getRoleName)
				.collect(Collectors.toSet()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		ModelMapper mapper = new ModelMapper();
		AccountDto accountDto = mapper.map(account, AccountDto.class);
		return new AccountContext(accountDto, authorities);
	}
}
