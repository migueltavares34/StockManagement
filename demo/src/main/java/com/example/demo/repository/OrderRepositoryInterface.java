package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Order;

public interface OrderRepositoryInterface extends JpaRepository<Order, Long> {
	
	@Query("SELECT o FROM Order o WHERE o.item.id = :id ORDER BY o.creationDate DESC")
	List<Order> getOrdersByItem(@Param("id") Long id);	
}
