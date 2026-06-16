package com.codewithmosh.store.repositories;

import com.codewithmosh.store.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> getOrderByCustomer_Id(Long id);
}