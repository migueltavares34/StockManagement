package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.UserBusiness;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.User;
import com.example.demo.model.request.UpdateUserRequest;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;

@Tag(name = "UserController", description = "Users management")
@RestController
@RequestMapping("/stockaccess/user")
public class UserController extends BaseController {

	@Autowired
	UserBusiness business;

	@GetMapping("/read")
	public ResponseEntity<BaseEntity> read(@RequestParam long id) {
		return read(User.builder().id(id).build());
	}

	@RolesAllowed({ "ADMIN", "SUPER_ADMIN" })
	@PatchMapping("/update")
	public ResponseEntity<BaseEntity> update(@RequestBody UpdateUserRequest updateUserRequest) {
		User user = User.builder().build();
		try {
			user = business.change(updateUserRequest.id(), updateUserRequest.email());
		} catch (Exception e) {
			user.setErrorMessage(e.getMessage());
		}

		return handleResult(user);
	}

	@RolesAllowed({ "SUPER_ADMIN" })
	@DeleteMapping("/delete")
	public ResponseEntity<BaseEntity> delete(@RequestParam long id) {
		return delete(User.builder().id(id).build());
	}
}
