package com.example.emenu.service.user;

import com.example.emenu.exception.BadRequestException;
import com.example.emenu.exception.ResourceNotFound;
import com.example.emenu.model.user.Authority;
import com.example.emenu.model.user.AuthorityEnum;
import com.example.emenu.model.user.RegisterRequest;
import com.example.emenu.model.user.User;
import com.example.emenu.model.user.UserDTO;
import com.example.emenu.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AuthService implements UserDetailsService {
	private final UserRepository userRepository;
	private final ModelMapper mapper;
	private final BCryptPasswordEncoder encoder;

	public AuthService(UserRepository userRepository, ModelMapper mapper,
			BCryptPasswordEncoder encoder) {
		this.userRepository = userRepository;
		this.mapper = mapper;
		this.encoder = encoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return this.getProfileByUserName(username).orElseThrow(() -> {
			log.error("User {} Not Found", username);
			throw new UsernameNotFoundException("User not Found");
		});
	}

	public Optional<User> getProfileByUserName(String username) {
		return this.userRepository.findByEmail(username);
	}

	public void registerUser(RegisterRequest registerRequest) {
		User newUser = mapper.map(registerRequest, User.class);
		getProfileByUserName(registerRequest.getEmail()).ifPresent(p -> {
			throw new BadRequestException(String.format("Username %s is used", registerRequest.getEmail()));
		});
		newUser.setPassword(encoder.encode(registerRequest.getPassword()));
		List<Authority> authorities = new ArrayList<>();
		authorities.add(Authority.createAuthority(AuthorityEnum.CREATE_MENU));
		authorities.add(Authority.createAuthority(AuthorityEnum.CREATE_USER));
		authorities.add(Authority.createAuthority(AuthorityEnum.MANAGEMENT));
		newUser.setAuthorities(authorities);

		userRepository.save(newUser);
	}

	public UserDTO getUser(String username, String token) {
		User user = this.userRepository.findByEmail(username)
				.orElseThrow(() -> new ResourceNotFound("User not found: " + username));

		UserDTO userDto = this.mapper.map(user, UserDTO.class);
		userDto.setToken(token);
		return userDto;
	}
}
