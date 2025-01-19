package com.community.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import com.community.config.security.dsl.RestApiDsl;
import com.community.config.security.entrypoint.RestAuthenticationEntryPoint;
import com.community.config.security.handler.RestAccessDeniedHandler;
import com.community.config.security.handler.RestAuthenticationFailureHandler;
import com.community.config.security.handler.RestAuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

//    private final AuthenticationProvider authenticationProvider;
	private final AuthenticationProvider restAuthenticationProvider;
//    private final AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;
	private final RestAuthenticationSuccessHandler restSuccessHandler;
	private final RestAuthenticationFailureHandler restFailureHandler;
//    private final AuthorizationManager<RequestAuthorizationContext> authorizationManager;

	@Bean
	public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		http.securityMatcher("/api/**")
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/css/**", "/images/**", "/js/**", "/favicon.*", "/*/icon-*").permitAll()
						.requestMatchers("/api/*/public/**").permitAll()
						.requestMatchers("/api/*/auth/**").authenticated()
						.requestMatchers("/api/*/member/**").hasAuthority("ROLE_USER")
						.requestMatchers("/api/*/admin/**").hasAuthority("ROLE_ADMIN")
						.anyRequest().authenticated())
				.csrf(AbstractHttpConfigurer::disable)
				.authenticationManager(authenticationManager)
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint(new RestAuthenticationEntryPoint())
						.accessDeniedHandler(new RestAccessDeniedHandler()))
				.with(new RestApiDsl<>(),
						restDsl -> restDsl
						.restSuccessHandler(restSuccessHandler)
						.restFailureHandler(restFailureHandler)
						.loginPage("/login")
						.loginProcessingUrl("/api/user/public/login"));

		return http.build();
	}
}
