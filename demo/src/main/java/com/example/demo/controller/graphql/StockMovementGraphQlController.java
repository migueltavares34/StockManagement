package com.example.demo.controller.graphql;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class StockMovementGraphQlController {

	@QueryMapping
	public String hello() {
		return "GraphQl";
	}
}
