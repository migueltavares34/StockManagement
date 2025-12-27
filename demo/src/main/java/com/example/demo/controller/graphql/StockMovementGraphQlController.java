package com.example.demo.controller.graphql;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.demo.business.StockMovementBusiness;
import com.example.demo.model.StockMovement;

@Controller
public class StockMovementGraphQlController {

	@Autowired
	StockMovementBusiness stockMovementBusiness;

	@QueryMapping
	public Collection<StockMovement> getAllStockMovements(){
		return stockMovementBusiness.getAllStockMovements();
	}
}
