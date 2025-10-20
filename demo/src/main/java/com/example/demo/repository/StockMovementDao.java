package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.StockMovement;

@Repository
public class StockMovementDao extends BaseDao{	

	public StockMovement create(StockMovement stockMovement) {
		return (StockMovement) presistEntity(stockMovement);
	}

	public StockMovement find(StockMovement stockMovement) {
		return (StockMovement) findEntity(stockMovement);
	}

	public StockMovement add(StockMovement stockMovement) {
		return (StockMovement) presistEntity(stockMovement);
	}

	public StockMovement change(StockMovement stockMovement) {
		BaseEntity returnedEntity = findEntity(stockMovement);
		((StockMovement) returnedEntity).setQuantity(stockMovement.getQuantity());
		return (StockMovement) changeEntity(returnedEntity);
	}

	public StockMovement delete(StockMovement stockMovement) {
		stockMovement = (StockMovement) findEntity(stockMovement);
		return (StockMovement) deleteEntity(stockMovement);
	}
}
