package com.example.emenu.configuration.security;

import com.example.emenu.model.user.LoginRequest;
import com.example.emenu.model.user.User;
import com.example.emenu.model.user.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authManager;
	private final JwtConfig jwtConfig;
	private final ModelMapper mapper;

	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager,
			JwtConfig jwtConfig, ModelMapper mapper) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
		this.mapper = mapper;
		// By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
		// In our case, we use "/auth". So, we need to override the defaults.
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {

			LoginRequest credentials = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
			log.info("Attempt to authenticate. {}", credentials.toString());
			AbstractAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credentials.getEmail(),
					credentials.getPassword(), Collections.emptyList());
			return authManager.authenticate(authToken);

		} catch (IOException e) { // TODO: catch more exceptions
			log.error("Authentication failed: {}", e.getMessage());
			throw new InternalAuthenticationServiceException(e.getMessage(), e);
		}
	}

	// Upon successful authentication, generate a token.
	// The 'auth' passed to successfulAuthentication() is the current authenticated logins.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) {

		log.info("Successful Authentication - building JWT");
		String token = jwtConfig.buildJwtToken(auth);

		response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
		response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, jwtConfig.getHeader());

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		ObjectMapper objectMapper = new ObjectMapper();
		User user = (User) auth.getPrincipal();
		UserDTO userDTO = this.mapper.map(user, UserDTO.class);

		userDTO.setToken(token);
		try {
			String stringProfile = objectMapper.writeValueAsString(userDTO);
			response.getWriter().write(stringProfile);
		} catch (IOException e) {
			log.error("Failed to add profile to response payload");
			e.printStackTrace();
		}
	}

	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) {
		log.debug("unsuccessful authentication: {}", failed.getMessage());
		Map<String, String> authError = new HashMap<>();
		ObjectMapper objectMapper = new ObjectMapper();
		authError.put("Message", failed.getMessage());
		try {
			response.getWriter().write(objectMapper.writeValueAsString(authError));
		} catch (IOException e) {
			log.error("Failed to add Message to response payload");
			e.printStackTrace();
		}
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
	}
}
