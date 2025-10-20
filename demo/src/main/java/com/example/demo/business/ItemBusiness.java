package com.example.demo.business;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemDao;
import com.example.demo.utils.ManageLogs;

@Component
public class ItemBusiness {

    private static final Logger logger = ManageLogs.getLogger();
    
	@Autowired
	ItemDao dao;

	public Item create(String name) {
		Item item = Item.builder().name(name).build();
		item = dao.create(item);
		return item;
	}

	public Item find(long itemId) {
		Item item = Item.builder().id(itemId).build();
		return dao.find(item);
	}

	public Item change(long itemId, String name) {
		Item item;

		item = Item.builder().id(itemId).name(name).build();
		item = dao.change(item);
		return item;
	}

	public Item delete(long id) {
		Item item = Item.builder().id(id).build();
		item = dao.delete(item);
		return item;
	}
}
