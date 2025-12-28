package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.business.AuthenticationBusiness;
import com.example.demo.model.User;
import com.example.demo.model.request.AuthRequest;
import com.example.demo.model.request.CreateUserRequest;
import com.example.demo.model.response.AuthResponse;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "AuthController", description = "SignUp or login into app")
@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	AuthenticationBusiness authenticationBusiness;

	@Autowired
	AuthenticationManager autenticationManager;

	@PostMapping("/signup")
	public ResponseEntity<User> register(@RequestBody CreateUserRequest createUserRequest) {
		return ResponseEntity.ok(authenticationBusiness.signup(new User(createUserRequest)));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {

		return ResponseEntity.ok(new AuthResponse(authenticate(request)));
	}

	public String authenticate(AuthRequest authRequest) {
		Authentication auth = autenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authRequest.userName(), authRequest.password()));

		return authenticationBusiness.generateToken((UserDetails) auth.getPrincipal());
	}
}
