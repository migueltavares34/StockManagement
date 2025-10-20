package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.Order;

@Repository
public class OrderDao extends BaseDao {

	public Order create(Order order) {
		return (Order) presistEntity(order);
	}

	public Order find(Order order) {
		return (Order) findEntity(order);
	}

	public Order change(Order order) {
		BaseEntity returnedEntity = findEntity(order);
		((Order) returnedEntity).setQuantity(order.getQuantity());
		return (Order) changeEntity(returnedEntity);
	}

	public Order delete(Order order) {
		order = (Order) findEntity(order);
		return (Order) deleteEntity(order);
	}
}
