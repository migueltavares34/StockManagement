package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;

@Repository
public class ItemDao extends BaseDao {

	public BaseEntity change(BaseEntity entity) {
		BaseEntity returnedEntity = findEntity(entity);
		((Item) returnedEntity).setName(((Item)entity).getName());
		return (BaseEntity) changeEntity(returnedEntity);
	}
}
