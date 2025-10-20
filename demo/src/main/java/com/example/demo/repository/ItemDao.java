package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;

@Repository
public class ItemDao extends BaseDao {

	public Item create(Item item) {
		return (Item) presistEntity(item);
	}

	public Item find(Item item) {
		return (Item) findEntity(item);
	}

	public Item change(Item item) {
		BaseEntity returnedEntity = findEntity(item);
		((Item) returnedEntity).setName(item.getName());
		return (Item) changeEntity(returnedEntity);
	}

	public Item delete(Item item) {
		item = (Item) findEntity(item);
		return (Item) deleteEntity(item);
	}
}
