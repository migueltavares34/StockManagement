package com.example.demo.business;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.model.Item;
import com.example.demo.repository.ItemRepositoryInterface;

@Service
@Qualifier("itemBusiness")
public class ItemBusiness extends BaseBusiness  {

	@Autowired
	private ItemRepositoryInterface itemRepositoryInterface;
	
	public Collection<Item> getAllItems() {
		return itemRepositoryInterface.findAll();
	}
    
}
