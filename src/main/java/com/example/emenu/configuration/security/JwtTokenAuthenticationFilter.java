package com.example.emenu.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;

	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		String header;
		if (request.getHeader(jwtConfig.getHeader()) != null) {
			header = request.getHeader(jwtConfig.getHeader());
		} else {
			String cookieValue = getCookieValue(request, jwtConfig.getHeader()).orElse(null);
			if (cookieValue != null) { // TODO remove when front end can generate access token
				header = jwtConfig.getPrefix() + cookieValue;
			} else {
				header = null;
			}
		}

		if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
			chain.doFilter(request, response);
			return;
		}

		String token = header.replace(jwtConfig.getPrefix(), "");

		try {
			Claims claims = jwtConfig.validateJwtToken(token);
			String username = claims.getSubject();

			if (username != null) {
				@SuppressWarnings("unchecked")
				List<String> authorities = (List<String>) claims.get("authorities");

				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
						username,
						null,
						authorities.stream()
								.map(SimpleGrantedAuthority::new)
								.collect(Collectors.toList()));

				SecurityContextHolder.getContext()
						.setAuthentication(auth);
			}

		} catch (UnsupportedJwtException | MalformedJwtException |
				SignatureException | IllegalArgumentException | ClassCastException e) {
			logger.info("Invalid token");
			SecurityContextHolder.clearContext();
		}

		chain.doFilter(request, response);
	}

	private static Optional<String> getCookieValue(HttpServletRequest request, String name) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return Optional.of(cookie.getValue());
				}
			}
		}
		return Optional.empty();
	}

}
