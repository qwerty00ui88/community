package com.community.config.security.filter;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;

import com.community.common.util.WebUtil;
import com.community.config.security.token.RestAuthenticationToken;
import com.community.user.application.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class RestAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	private final ObjectMapper objectMapper = new ObjectMapper();

	public RestAuthenticationFilter() {
		super(new AntPathRequestMatcher("/api/user/public/login", "POST"));
	}

	public SecurityContextRepository getSecurityContextRepository(HttpSecurity http) {
		SecurityContextRepository securityContextRepository = http.getSharedObject(SecurityContextRepository.class);
		if (securityContextRepository == null) {
			securityContextRepository = new DelegatingSecurityContextRepository(
					new RequestAttributeSecurityContextRepository(), new HttpSessionSecurityContextRepository());
		}
		return securityContextRepository;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException {

		if (!HttpMethod.POST.name().equals(request.getMethod()) || !WebUtil.isAjax(request)) {
			throw new IllegalArgumentException("Authentication method not supported");
		}

		UserDTO accountDto = objectMapper.readValue(request.getReader(), UserDTO.class);

		if (!StringUtils.hasText(accountDto.getNickname()) || !StringUtils.hasText(accountDto.getPassword())) {
			throw new AuthenticationServiceException("Username or Password not provided");
		}
		RestAuthenticationToken token = new RestAuthenticationToken(accountDto.getNickname(), accountDto.getPassword());

		return this.getAuthenticationManager().authenticate(token);
	}

}
