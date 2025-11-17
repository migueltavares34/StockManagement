package com.example.demo.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.OrderBusiness;
import com.example.demo.model.Order;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "OrderController", description = "Orders management")
@RestController
@RequestMapping("/stockaccess/order")
public class OrderController  extends BaseController{
	
	OrderController(){
		logger = LoggerFactory.getLogger(OrderController.class);
	}

	@Autowired
	OrderBusiness business;

	@Tag(name = "Create order", description = "Create order and if there is enugh stock of the item it will be closed immediately and stock movement's will be adjusted or removed if empty")
	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestParam Long userId, Long itemId, Long quantity) {
		Order order = Order.builder().build();
		try {
			order = business.create(userId, itemId, quantity);
		} catch (Exception e) {
			order.setErrorMessage(e.getMessage());
		}

		return handleResult(order, "Order created");
	}

	@Tag(name = "Find order")
	@GetMapping("/find")
	public ResponseEntity<String> find(@RequestParam long id) {
		Order order = Order.builder().build();
		try {
			order = business.find(id);
		} catch (Exception e) {
			order.setErrorMessage(e.getMessage());
		}

		return handleResult(order, "Order found");
	}

	@Tag(name = "Change order")
	@PutMapping("/change")
	public ResponseEntity<String> change(@RequestParam long id, @RequestParam long quantity) {
		Order order = Order.builder().build();
		try {
			order = business.change(id, quantity);
		} catch (Exception e) {
			order.setErrorMessage(e.getMessage());
		}

		return handleResult(order, "Order quantity changed");
	}

	@Tag(name = "Remove order")
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam long id) {
		Order order = Order.builder().build();
		try {
			order = business.delete(id);
		} catch (Exception e) {
			order.setErrorMessage(e.getMessage());
		}
		return handleResult(order, "Order deleted");
	}
}
