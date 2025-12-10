package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.StockMovement;

@Repository
public class StockMovementDao extends BaseDao{	

	public StockMovement change(StockMovement stockMovement) {
		StockMovement returnedEntity = (StockMovement) findEntity(stockMovement);
		returnedEntity.setQuantity(stockMovement.getQuantity());
		return (StockMovement) changeEntity(returnedEntity);
	}

	public StockMovement delete(StockMovement stockMovement) {
		stockMovement = (StockMovement) findEntity(stockMovement);
		return (StockMovement) deleteEntity(stockMovement);
	}
}
