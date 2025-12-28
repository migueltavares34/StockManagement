package com.example.demo.controller.graphql;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.demo.business.OrderBusiness;
import com.example.demo.model.Order;

@Controller
public class ItemGraphQlController {

	@Autowired
	OrderBusiness orderBusiness;

	@QueryMapping
	public Collection<Order> getAllOrders() {
		return orderBusiness.getAllOrders();
	}
}
