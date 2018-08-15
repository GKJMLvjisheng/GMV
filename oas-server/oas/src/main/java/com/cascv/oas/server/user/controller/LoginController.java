package com.cascv.oas.server.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cascv.oas.server.utils.ServletUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(value="user interface")
@RequestMapping(value="/userCenter")
public class LoginController {
	@ApiOperation(value="user login", notes="")
	@GetMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		if (ServletUtils.isAjaxRequest(request)){
			return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"鏈櫥褰曟垨鐧诲綍瓒呮椂銆傝閲嶆柊鐧诲綍\"}");
		}
		return "login";
	}
}