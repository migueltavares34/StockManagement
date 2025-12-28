package com.example.demo.controller.graphql;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.demo.business.ItemBusiness;
import com.example.demo.model.Item;

@Controller
public class OrderGraphQlController {

	@Autowired
	ItemBusiness itemBusiness;

	@QueryMapping
	public Collection<Item> getAllItems(){
		return itemBusiness.getAllItems();
	}
}
