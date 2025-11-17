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

import com.example.demo.business.StockMovementBusiness;
import com.example.demo.model.StockMovement;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "StockMovements", description = "Stock movements management")
@RestController
@RequestMapping("/stockaccess/stockmovement")
public class StockMovementController extends BaseController {

	StockMovementController() {
		logger = LoggerFactory.getLogger(StockMovementController.class);
	}

	@Autowired
	StockMovementBusiness business;

	@Tag(name = "Create stock movement")
	@PostMapping("/create")
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
	@PutMapping("/add")
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
	@PutMapping("/change")
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
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam long id) {
		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.delete(id);
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getMessage());
		}
		return handleResult(stockMovement, "StockMovement deleted");
	}
}
