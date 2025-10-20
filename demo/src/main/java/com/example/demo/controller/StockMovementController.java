package com.example.demo.controller;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.StockMovementBusiness;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.StockMovement;
import com.example.demo.utils.ManageLogs;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "StockMovements", description = "Stock movements management")
@RestController
@RequestMapping("/stockaccess/stockmovement")
public class StockMovementController {

	private static final Logger logger = ManageLogs.getLogger();

	@Autowired
	StockMovementBusiness business;

	@Tag(name = "Create stock movement")
	@GetMapping("/create")
	public ResponseEntity<String> create(@RequestParam Long itemId, @RequestParam Long quantity) {
		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.create(itemId, quantity);
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getMessage());
		}

		return handleResult(stockMovement, "StockMovement created");
	}

	@Tag(name = "Add stock Movement", description = "Checks if there are orders that can be fullfiled and if there is remaining quantity it will add to existing stock movement or create new one")
	@GetMapping("/add")
	public ResponseEntity<String> add(@RequestParam Long itemId, @RequestParam Long quantity) {

		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.add(itemId, quantity);
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getClass().getName() + " " + e.getMessage());
		}

		return handleResult(stockMovement, "StockMovement created");
	}

	@Tag(name = "Find stock movement")
	@GetMapping("/find")
	public ResponseEntity<String> find(@RequestParam long id) {
		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.find(id);
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getMessage());
		}

		return handleResult(stockMovement, "StockMovement found");
	}

	@Tag(name = "Change stock movement")
	@GetMapping("/change")
	public ResponseEntity<String> change(@RequestParam long id, @RequestParam long quantity) {
		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.change(id, quantity);
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getMessage());
		}

		return handleResult(stockMovement, "StockMovement quantity changed");
	}

	@Tag(name = "Remove stock movement")
	@GetMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam long id) {
		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.delete(id);
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getMessage());
		}
		return handleResult(stockMovement, "StockMovement deleted");
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
