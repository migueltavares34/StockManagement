package com.example.demo.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.OrderBusiness;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.Order;

@RestController
@RequestMapping("/stockaccess/order")
public class OrderController {

	protected static final Logger logger = LogManager.getLogger();

	@Autowired
	OrderBusiness business;

	@GetMapping("/create")
	public ResponseEntity<String> create(@RequestParam Long userId, Long itemId, Long quantity) {
		Order order = Order.builder().build();
		try {
			order = business.create(userId, itemId, quantity);
		} catch (Exception e) {
			order.setErrorMessage(e.getMessage());
		}

		return handleResult(order, "Order created");
	}

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

	@GetMapping("/change")
	public ResponseEntity<String> change(@RequestParam long id, @RequestParam long quantity) {
		Order order = Order.builder().build();
		try {
			order = business.change(id, quantity);
		} catch (Exception e) {
			order.setErrorMessage(e.getMessage());
		}

		return handleResult(order, "Order quantity changed");
	}

	@GetMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam long id) {
		Order order = Order.builder().build();
		try {
			order = business.delete(id);
		} catch (Exception e) {
			order.setErrorMessage(e.getMessage());
		}
		return handleResult(order, "Order deleted");
	}

	private ResponseEntity<String> handleResult(BaseEntity entity, String successMessage) {

		if (entity == null) {
			logger.info("Entity is null");
			return new ResponseEntity<String>("Order not found", HttpStatus.BAD_REQUEST);

		} else if (StringUtils.isNotBlank(entity.getErrorMessage())) {
			logger.info(entity.getErrorMessage());
			return new ResponseEntity<String>(entity.getErrorMessage(), HttpStatus.BAD_REQUEST);
		}
		successMessage = successMessage + ": " + entity.toString();
		logger.info(successMessage);
		return new ResponseEntity<String>(successMessage, HttpStatus.CREATED);
	}
}
