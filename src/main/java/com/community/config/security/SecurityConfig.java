package com.community.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

	private final AuthenticationProvider restAuthenticationProvider;
	private final RestAuthenticationSuccessHandler restSuccessHandler;
	private final RestAuthenticationFailureHandler restFailureHandler;

	@Bean
	public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {

		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.authenticationProvider(restAuthenticationProvider);
		AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

		http.securityMatcher("/**")
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/WEB-INF/**", "/static/**").permitAll()
						.requestMatchers("/", "/login", "/signup", "/category/**", "/board/**", "/search/**", "/api/*/public/**").permitAll()
						.requestMatchers("/profile/**", "/api/*/auth/**").authenticated()
//						.requestMatchers("/api/*/member/**").hasAuthority("ROLE_USER")
						.requestMatchers("/admin/**", "/api/*/admin/**").hasAuthority("ROLE_ADMIN")
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
						.loginProcessingUrl("/api/account/public/login"));

		return http.build();
	}
}
