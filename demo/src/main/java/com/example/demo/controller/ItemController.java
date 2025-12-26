package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.BaseEntity;
import com.example.demo.model.Item;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;

@Tag(name = "ItemController", description = "Items management")
@RestController
@RequestMapping("/stockaccess/item")
public class ItemController extends BaseController {

	@PostMapping("/create")
	public ResponseEntity<BaseEntity> create(@RequestParam String name) {
		return create(Item.builder().name(name).build());
	}

	@GetMapping("/find")
	public ResponseEntity<BaseEntity> read(@RequestParam long id) {
		return read(Item.builder().id(id).build());
	}


	@RolesAllowed({"ADMIN","SUPER_ADMIN"})
	@PatchMapping("/change")
	public ResponseEntity<BaseEntity> change(@RequestParam long id, @RequestParam String name) {
		return update(Item.builder().id(id).name(name).build());
	}

	@RolesAllowed({"ADMIN","SUPER_ADMIN"})
	@DeleteMapping("/delete")
	public ResponseEntity<BaseEntity> delete(@RequestParam long id) {
		return delete(Item.builder().id(id).build());
	}
}
