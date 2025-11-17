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

import com.example.demo.business.UserBusiness;
import com.example.demo.model.User;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "UserController", description = "Users management")
@RestController
@RequestMapping("/stockaccess/user")
public class UserController extends BaseController {

	UserController() {
		logger = LoggerFactory.getLogger(UserController.class);
	}

	@Autowired
	UserBusiness business;

	@Tag(name = "Create user")
	@PostMapping("/create")
	public ResponseEntity<String> create(@RequestParam String name, @RequestParam String email) {
		User user = User.builder().build();
		try {
			user = business.create(name, email);
		} catch (Exception e) {
			user.setErrorMessage(e.getMessage());
		}

		return handleResult(user, "User created");
	}

	@Tag(name = "Find user")
	@GetMapping("/find")
	public ResponseEntity<String> find(@RequestParam long id) {
		return find(User.builder().id(id).build());
	}

	@Tag(name = "Change user")
	@PutMapping("/change")
	public ResponseEntity<String> change(@RequestParam long id, @RequestParam String email) {
		User user = User.builder().build();
		try {
			user = business.change(id, email);
		} catch (Exception e) {
			user.setErrorMessage(e.getMessage());
		}

		return handleResult(user, "User email changed");
	}

	@Tag(name = "Remove user")
	@DeleteMapping("/delete")
	public ResponseEntity<String> delete(@RequestParam long id) {
		return delete(User.builder().id(id).build());
	}
}
