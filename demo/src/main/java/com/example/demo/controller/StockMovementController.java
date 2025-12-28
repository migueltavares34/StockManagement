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
import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;
import com.example.demo.model.StockMovement;
import com.example.demo.model.request.AddStockMovementRequest;
import com.example.demo.model.request.CreateStockMovementRequest;
import com.example.demo.model.request.FullfillAddOrCreateStockMovementRequest;
import com.example.demo.model.request.UpdateStockMovementRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;

@Tag(name = "StockMovements", description = "Stock movements management")
@RestController
@RequestMapping("/stockaccess/stockmovement")
public class StockMovementController extends BaseController {

	@Autowired
	StockMovementBusiness business;

	@PostMapping("/create")
	public ResponseEntity<BaseEntity> create(@RequestBody CreateStockMovementRequest createStockMovementRequest) {
		Item item = Item.builder().id(createStockMovementRequest.itemId()).build();
		BaseEntity entity = StockMovement.builder().item(item).quantity(createStockMovementRequest.quantity())
				.creationDate(new Date()).build();
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
	public ResponseEntity<BaseEntity> update(@RequestBody UpdateStockMovementRequest updateStockMovementRequest) {
		return update(StockMovement.builder().id(updateStockMovementRequest.id())
				.quantity(updateStockMovementRequest.quantity()).build());
	}

	@RolesAllowed({ "ADMIN", "SUPER_ADMIN" })
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
	public ResponseEntity<BaseEntity> fullfillAddOrCreate(
			@RequestBody FullfillAddOrCreateStockMovementRequest fullfillAddOrCreateStockMovementRequest) {

		StockMovement stockMovement = StockMovement.builder().build();
		try {
			stockMovement = business.fullfillAddOrCreate(fullfillAddOrCreateStockMovementRequest.itemId(),
					fullfillAddOrCreateStockMovementRequest.quantity());
		} catch (Exception e) {
			stockMovement.setErrorMessage(e.getClass().getName() + " " + e.getMessage());
		}

		return handleResult(stockMovement);
	}
}
