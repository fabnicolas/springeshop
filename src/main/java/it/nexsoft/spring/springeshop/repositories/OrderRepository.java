package it.nexsoft.spring.springeshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import it.nexsoft.spring.springeshop.models.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
