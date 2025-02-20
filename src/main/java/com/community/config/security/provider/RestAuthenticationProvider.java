package com.community.config.security.provider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.amazonaws.services.kms.model.DisabledException;
import com.community.account.application.dto.AccountContext;
import com.community.account.domain.AccountStatus;
import com.community.config.security.token.RestAuthenticationToken;

import lombok.RequiredArgsConstructor;

@Component("restAuthenticationProvider")
@RequiredArgsConstructor
public class RestAuthenticationProvider implements AuthenticationProvider {

	private final UserDetailsService userDetailsService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

		String loginId = authentication.getName();
		String password = (String) authentication.getCredentials();
		AccountContext accountContext = (AccountContext) userDetailsService.loadUserByUsername(loginId);

		if (!passwordEncoder.matches(password, accountContext.getPassword())) {
			throw new BadCredentialsException("Invalid password");
		}

		switch (accountContext.getStatus()) {
		case INACTIVE:
			throw new DisabledException("계정이 비활성화되었습니다.");
		case DELETED:
			throw new DisabledException("탈퇴한 계정입니다.");
		default:
			if (accountContext.getStatus() != AccountStatus.ACTIVE) {
				throw new AuthenticationException("알 수 없는 사용자 상태입니다.") {
				};
			}
		}

		return new RestAuthenticationToken(accountContext.getAuthorities(), accountContext.getAccountDto(), null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.isAssignableFrom(RestAuthenticationToken.class);
	}
}
