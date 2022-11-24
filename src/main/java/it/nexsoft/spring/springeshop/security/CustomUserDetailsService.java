package it.nexsoft.spring.springeshop.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import it.nexsoft.spring.springeshop.models.User;
import it.nexsoft.spring.springeshop.repositories.UserRepository;

public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(final String username) {
		final Optional<User> userModelOpt = Optional.ofNullable(userRepository.findUserByEmail(username));
		final User userModel = userModelOpt
				.orElseThrow(() -> new UsernameNotFoundException("User not found in the system."));
		return new CustomUserDetails(userModel);
	}

}