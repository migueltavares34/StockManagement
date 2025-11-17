package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Item;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "ItemController", description = "Items management")
@RestController
@RequestMapping("/stockaccess/item")
public class ItemController extends BaseController {

	@Tag(name = "Create item")
	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestParam String name) {
		return create(Item.builder().name(name).build());
	}

	@Tag(name = "Find item")
	@GetMapping("/find")
	public ResponseEntity<String> find(@RequestParam long id) {
		return find(Item.builder().id(id).build());
	}

	@Tag(name = "Change item")
	@PutMapping("/change")
	public ResponseEntity<String> change(@RequestParam long id, @RequestParam String name) {
		return change(Item.builder().id(id).name(name).build());
	}

	@Tag(name = "Remove item")
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam long id) {
		return delete(Item.builder().id(id).build());
	}
}
