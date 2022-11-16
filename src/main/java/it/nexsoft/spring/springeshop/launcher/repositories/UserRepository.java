package it.nexsoft.spring.springeshop.launcher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.nexsoft.spring.springeshop.launcher.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
