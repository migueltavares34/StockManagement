package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.Order;

@Repository
public class OrderDao extends BaseDao {

	public BaseEntity change(BaseEntity entity) {
		BaseEntity returnedEntity = findEntity(entity);
		((Order) returnedEntity).setQuantity(((Order) entity).getQuantity());
		return (BaseEntity) changeEntity(returnedEntity);
	}
}
