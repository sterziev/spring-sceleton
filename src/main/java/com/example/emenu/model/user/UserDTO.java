package com.example.emenu.model.user;

import com.example.emenu.model.AbstractAuditingDocument;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO{
	private String token;
	private String uuid;
	private String firstName;
	private String lastName;
	private String email;
	private List<GrantedAuthority> authorities;
}
