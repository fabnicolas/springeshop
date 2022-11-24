package it.nexsoft.spring.springeshop.security.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPswGenLauncher {

	public static void main(final String[] args) {
		final String hashedPassword = new BCryptPasswordEncoder().encode("123456");
		System.out.println(hashedPassword);
	}
}