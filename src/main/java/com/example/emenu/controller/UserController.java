package com.example.emenu.controller;

import com.example.emenu.model.user.RegisterRequest;
import com.example.emenu.model.user.User;
import com.example.emenu.service.user.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private AuthService authService;

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public void registerUser(@RequestBody @Valid RegisterRequest registerRequest) {
		this.authService.registerUser(registerRequest);
	}

	@GetMapping("/me")
	@PreAuthorize("hasAuthority('READ_ONLY')")
	public String redirect(@AuthenticationPrincipal String username, HttpServletRequest request) {
		//        String token = CookieUtils.getCookieValue(request, AUTHORIZATION_HEADER).orElse(null);
		//        ProfileDto profileDto = this.authService.getProfileDto(username, token);
		return "Ok";
	}
}
