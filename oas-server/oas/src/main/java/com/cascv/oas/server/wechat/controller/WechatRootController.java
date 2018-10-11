package com.cascv.oas.server.wechat.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class WechatRootController {
		
	@GetMapping(value="/getIdentifyCode")
	public String home() {
		return "getIdentifyCode";
	}
}

