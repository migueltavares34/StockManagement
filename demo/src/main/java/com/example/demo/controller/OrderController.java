package com.example.demo.controller;

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
import com.example.demo.model.BaseEntity;
import com.example.demo.model.Order;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;

@Tag(name = "OrderController", description = "Orders management")
@RestController
@RequestMapping("/stockaccess/order")
public class OrderController extends BaseController {

	@Autowired
	OrderBusiness business;

	@Tag(name = "Create order", description = "Create order and if there is enugh stock of the item it will be closed immediately and stock movement's will be adjusted or removed if empty")
	@PostMapping("/create")
	public ResponseEntity<BaseEntity> create(@RequestParam Long userId, Long itemId, Long quantity) {
		Order order = Order.builder().build();
		try {
			order = (Order) business.create(userId, itemId, quantity);
		} catch (Exception e) {
			order.setErrorMessage(e.getMessage());
		}

		return handleResult(order);
	}

	@GetMapping("/find")
	public ResponseEntity<BaseEntity> read(@RequestParam long id) {
		return read(Order.builder().id(id).build());
	}

	@RolesAllowed({"ADMIN","SUPER_ADMIN"})
	@PutMapping("/change")
	public ResponseEntity<BaseEntity> change(@RequestParam long id, @RequestParam long quantity) {
		return update(Order.builder().id(id).quantity(quantity).build());
	}

	@RolesAllowed({"ADMIN","SUPER_ADMIN"})
	@DeleteMapping("/delete")
	public ResponseEntity<BaseEntity> delete(@RequestParam long id) {
		return delete(Order.builder().id(id).build());
	}
}
