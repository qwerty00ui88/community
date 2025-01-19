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

import com.community.user.application.dto.AccountContext;
import com.community.user.application.dto.UserDTO;
import com.community.user.domain.Role;
import com.community.user.domain.UserEntity;
import com.community.user.domain.UserRepository;

import lombok.RequiredArgsConstructor;

@Service("userDetailsService")
@RequiredArgsConstructor
public class FormUserDetailsService implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UserEntity account = userRepository.findByNickname(username);
		if (account == null) {
			throw new UsernameNotFoundException("No user found with username: " + username);
		}
		List<GrantedAuthority> authorities = account.getUserRoles().stream().map(Role::getRoleName)
				.collect(Collectors.toSet()).stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		ModelMapper mapper = new ModelMapper();
		UserDTO accountDto = mapper.map(account, UserDTO.class);

		return new AccountContext(accountDto, authorities);
	}
}
