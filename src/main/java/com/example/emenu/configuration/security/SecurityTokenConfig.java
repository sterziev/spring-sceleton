package com.example.emenu.configuration.security;

import com.example.emenu.service.user.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	private final JwtConfig jwtConfig;
	private final ModelMapper mapper;
	private final AuthService authService;
	private final BCryptPasswordEncoder encoder;
	private final RoleHierarchy roleHierarchy;

	public SecurityTokenConfig(JwtConfig jwtConfig, ModelMapper mapper,
			AuthService authService, BCryptPasswordEncoder encoder,
			RoleHierarchy roleHierarchy) {
		this.jwtConfig = jwtConfig;
		this.mapper = mapper;
		this.authService = authService;
		this.encoder = encoder;
		this.roleHierarchy = roleHierarchy;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors()
				.configurationSource(request -> createCorsConfig())
				.and()
				.csrf()
				.disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
				.and()
				.addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, mapper))
				.addFilterBefore(new JwtTokenAuthenticationFilter(jwtConfig),
						UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				.expressionHandler(webExpressionHandler())
				.antMatchers("/user/register", "/public/**").permitAll()
				.antMatchers("/v2/api-docs",
						"/swagger-resources/**",
						"/swagger-ui.html",
						"/webjars/**",
						"/swagger.json").permitAll()
				.antMatchers("/login/**", "/oauth2/**").permitAll()
				.anyRequest().authenticated();
		//				.and()
		//				.oauth2Login()
		//				.authorizationEndpoint()
		//				.baseUri("/oauth2/authorize")
		//				.and()
		//				.userInfoEndpoint()
		//				.userService(customOAuth2UserService)
		//				.and()
		//				.successHandler(oAuth2AuthenticationSuccessHandler);
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authService).passwordEncoder(encoder);
	}

	private CorsConfiguration createCorsConfig() {
		CorsConfiguration corsConfig = new CorsConfiguration().applyPermitDefaultValues();
		corsConfig.setAllowedOrigins(new ArrayList<>());
		corsConfig.addAllowedOrigin("http://localhost:8100");
		corsConfig.addAllowedOrigin("http://localhost");

		corsConfig.addAllowedMethod(HttpMethod.DELETE);
		corsConfig.addAllowedMethod(HttpMethod.PUT);
		corsConfig.addAllowedMethod(HttpMethod.PATCH);

		return corsConfig;
	}

	private SecurityExpressionHandler<FilterInvocation> webExpressionHandler() {
		DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
		defaultWebSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
		return defaultWebSecurityExpressionHandler;
	}
}
