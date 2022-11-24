package it.nexsoft.spring.springeshop.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import it.nexsoft.spring.springeshop.models.User;

public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	private final User userModel;

	public CustomUserDetails(final User userModel) {
		this.userModel = userModel;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(() -> "read");
	}

	@Override
	public String getUsername() {
		return userModel.getEmail();
	}

	@Override
	public String getPassword() {
		return userModel.getPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}