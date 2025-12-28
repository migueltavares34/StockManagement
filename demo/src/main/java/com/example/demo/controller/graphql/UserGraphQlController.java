package com.example.demo.controller.graphql;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import com.example.demo.business.UserBusiness;
import com.example.demo.model.User;

import jakarta.annotation.security.RolesAllowed;

@Controller
public class UserGraphQlController {

	@Autowired
	UserBusiness userBusiness;

	@QueryMapping
	@RolesAllowed("SUPER_ADMIN")
	public Collection<User> getAllUsers() {
		return userBusiness.getAllUsers();
	}
}
