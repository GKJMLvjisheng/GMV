package com.cascv.oas.server.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {
	
	@GetMapping(value="/")
	public String home() {
		return "redirect:/swagger-ui.html";
	}
}

