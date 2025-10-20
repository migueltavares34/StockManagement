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

import com.example.demo.business.ItemBusiness;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;
import com.example.demo.utils.ManageLogs;

@RestController
@RequestMapping("/stockaccess/item")
public class ItemController {

    private static final Logger logger = ManageLogs.getLogger();
	
	@Autowired
	ItemBusiness business;

	@GetMapping("/create")
	public ResponseEntity<String> create(@RequestParam String name) {
		Item item = Item.builder().build();
		try {
			item = business.create(name);
		} catch (Exception e) {
			item.setErrorMessage(e.getMessage());
		}

		return handleResult(item,"Item created");
	}

	@GetMapping("/find")
	public ResponseEntity<String> find(@RequestParam long id) {
		Item item = Item.builder().build();
		try {
			item = business.find(id);
		} catch (Exception e) {
			item.setErrorMessage(e.getMessage());
		}

		return handleResult(item,"Item found");
	}
	
	@GetMapping("/change")
	public ResponseEntity<String> change(@RequestParam long id, @RequestParam String name) {
		Item item = Item.builder().build();
		try {
			item = business.change(id, name);
		} catch (Exception e) {
			item.setErrorMessage(e.getMessage());
		}		

		return handleResult(item,"Item name changed");
	}		
	
	@GetMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam long id) {
		Item item = Item.builder().build();
		try {
			item = business.delete(id);
		} catch (Exception e) {
			item.setErrorMessage(e.getMessage());
		}
		return handleResult(item,"Item deleted");
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
