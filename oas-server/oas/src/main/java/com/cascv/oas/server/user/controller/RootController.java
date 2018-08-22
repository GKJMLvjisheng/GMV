package com.cascv.oas.server.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.cascv.oas.server.utils.ServletUtils;

import io.swagger.annotations.ApiOperation;

@Controller
public class RootController {
	
	@GetMapping(value="/")
	public String home() {
		return "redirect:/swagger-ui.html";
	}
	
  // Login 
  @ApiOperation(value="user login", notes="")
  @GetMapping("/login")
  public String login(HttpServletRequest request, HttpServletResponse response) {
    if (ServletUtils.isAjaxRequest(request)){
      return ServletUtils.renderString(response, "{\"code\":\"1\",\"message\":\"未认证\"}");
    }
    return "login";
  }
}

