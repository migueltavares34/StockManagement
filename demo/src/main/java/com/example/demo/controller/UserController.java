package com.example.demo.controller;

import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.UserBusiness;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.User;
import com.example.demo.utils.ManageLogs;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "UserController", description = "Users management")
@RestController
@RequestMapping("/stockaccess/user")
public class UserController {

	private static final Logger logger = ManageLogs.getLogger();

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
		User user = User.builder().build();
		try {
			user = business.find(id);
		} catch (Exception e) {
			user.setErrorMessage(e.getMessage());
		}

		return handleResult(user, "User found");
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
		User user = User.builder().build();
		try {
			user = business.delete(id);
		} catch (Exception e) {
			user.setErrorMessage(e.getMessage());
		}
		return handleResult(user, "User deleted");
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
