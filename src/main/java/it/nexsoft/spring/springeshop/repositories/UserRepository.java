package it.nexsoft.spring.springeshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.nexsoft.spring.springeshop.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
