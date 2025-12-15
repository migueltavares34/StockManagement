package com.example.demo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.StockMovementBusiness;
import com.example.demo.dto.request.AddStockMovementRequest;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;
import com.example.demo.model.StockMovement;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "StockMovements", description = "Stock movements management")
@RestController
@RequestMapping("/stockaccess/stockmovement")
public class StockMovementController extends BaseController {

	@Autowired
	StockMovementBusiness business;

	@PostMapping("/create")
	public ResponseEntity<BaseEntity> create(@RequestParam Long itemId, @RequestParam Long quantity) {
		Item item = Item.builder().id(itemId).build();
		BaseEntity entity = StockMovement.builder().item(item).quantity(quantity).creationDate(new Date()).build();
		try {
			entity = business.create(entity);
		} catch (Exception e) {
			entity.setErrorMessage(e.getMessage());
		}

		return handleResult(entity);
	}

	@GetMapping("/read")
	public ResponseEntity<BaseEntity> read(@RequestParam long id) {
		return read(StockMovement.builder().id(id).build());
	}

	@PatchMapping("/update")
	public ResponseEntity<BaseEntity> update(@RequestParam long id, @RequestParam long quantity) {
		return update(StockMovement.builder().id(id).quantity(quantity).build());
	}

	@DeleteMapping("/delete")
	public ResponseEntity<BaseEntity> delete(@RequestParam long id) {
		return delete(StockMovement.builder().id(id).build());
	}

	@Tag(name = "Add stock Movement", description = "Checks if stock movement exists, if it doesn't exist it will throw error!")
	@PutMapping("/add")
	public ResponseEntity<StockMovement> add(@RequestBody AddStockMovementRequest addStockMovementRequest) {

		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.add(addStockMovementRequest);
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getClass().getName() + " " + e.getMessage());
		}

		return new ResponseEntity(stockMovement, HttpStatus.CREATED);
	}

	@Tag(name = "Add or create stock Movement", description = "Checks if there are orders that can be fullfiled and if there is remaining quantity it will add to existing stock movement or create new one")
	@PutMapping(value = "/fullfill-add-or-create", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<BaseEntity> addOrCreate(@RequestParam Long itemId, @RequestParam Long quantity) {

		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.fullfilladdOrCreate(itemId, quantity);
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getClass().getName() + " " + e.getMessage());
		}

		return handleResult(stockMovement);
	}
}
