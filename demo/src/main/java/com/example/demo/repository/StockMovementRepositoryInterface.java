package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.StockMovement;

public interface StockMovementRepositoryInterface extends JpaRepository<StockMovement, Long> {
	
	@Query("SELECT mov FROM StockMovement mov WHERE mov.item.id = :id")
	StockMovement getStockMovementByItem(@Param("id") Long id);
	
}
