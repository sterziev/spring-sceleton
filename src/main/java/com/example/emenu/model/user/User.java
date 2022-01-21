package com.example.emenu.model.user;

import com.example.emenu.model.AbstractAuditingDocument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractAuditingDocument implements UserDetails {
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private List<Authority> authorities;

	@Override public String getUsername() {
		return email;
	}

	@Override public boolean isAccountNonExpired() {
		return true;
	}

	@Override public boolean isAccountNonLocked() {
		return true;
	}

	@Override public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override public boolean isEnabled() {
		return true;
	}
}
