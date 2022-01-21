package com.example.emenu.configuration.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class SecurityBeans {
	@Bean
	public JwtConfig JWT() {
		return new JwtConfig();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public RoleHierarchy roleHierarchy() {
		RoleHierarchyImpl r = new RoleHierarchyImpl();
		r.setHierarchy("INTERNAL > MANAGEMENT > CREATE_USER > CREATE_MENU > READ_ONLY");
		return r;
	}

	@Bean
	public AffirmativeBased defaultAccessDecisionManager(RoleHierarchy roleHierarchy){
		log.info("arrive public AffirmativeBased defaultAccessDecisionManager()");
		List<AccessDecisionVoter<?>> decisionVoters = new ArrayList<>();

		// webExpressionVoter
		WebExpressionVoter webExpressionVoter = new WebExpressionVoter();
		DefaultWebSecurityExpressionHandler
				expressionHandler = new DefaultWebSecurityExpressionHandler();
		expressionHandler.setRoleHierarchy(roleHierarchy);
		webExpressionVoter.setExpressionHandler(expressionHandler);

		decisionVoters.add(webExpressionVoter);
		decisionVoters.add(roleHierarchyVoter(roleHierarchy));
		// return new AffirmativeBased(Arrays.asList((AccessDecisionVoter) webExpressionVoter));
		return new AffirmativeBased(decisionVoters);
	}

	@Bean
	public RoleHierarchyVoter roleHierarchyVoter(RoleHierarchy roleHierarchy) {
		log.info("arrive public RoleHierarchyVoter roleHierarchyVoter");
		return new RoleHierarchyVoter(roleHierarchy);
	}
}
