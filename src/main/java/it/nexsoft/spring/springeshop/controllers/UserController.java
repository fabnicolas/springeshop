package it.nexsoft.spring.springeshop.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.nexsoft.spring.springeshop.models.User;
import it.nexsoft.spring.springeshop.repositories.UserRepository;

@RestController
@RequestMapping("/prova")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/getallusers")
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> users = new ArrayList<>();
			users = userRepository.findAll();
			if (users.isEmpty())
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);

			return new ResponseEntity<>(users, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/postuser")
	public ResponseEntity<User> addUser(@RequestBody final User user) {
		try {
			userRepository.save(user);
			return new ResponseEntity<>(user, HttpStatus.CREATED);
		} catch (final Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/putuser/{id}")
	public ResponseEntity<User> updateUser(@PathVariable final Long id, @RequestBody final User user) {

		final Optional<User> userData = userRepository.findById(id);

		if (userData.isPresent()) {
			final User _user = userData.get();
			_user.setFirstName(user.getFirstName());
			_user.setLastName(user.getLastName());
			_user.setEmail(user.getEmail());
			_user.setPhone(user.getPhone());
			return new ResponseEntity<>(userRepository.save(_user), HttpStatus.OK);
		} else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@DeleteMapping("/deleteuser/{id}")
	public ResponseEntity<HttpStatus> deleteUser(@PathVariable final Long id) {
		try {
			userRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (final Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/deleteallusers")
	public ResponseEntity<HttpStatus> deleteAllUsers() {
		try {
			userRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (final Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
