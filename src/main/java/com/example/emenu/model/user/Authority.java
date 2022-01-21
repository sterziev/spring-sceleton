package com.example.emenu.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
public class Authority implements GrantedAuthority {
	private String authority;

	public static Authority createAuthority(AuthorityEnum authorityEnum) {
		return new Authority(authorityEnum.toString());
	}
}
