package it.nexsoft.spring.springeshop.launcher.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.nexsoft.spring.springeshop.launcher.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
