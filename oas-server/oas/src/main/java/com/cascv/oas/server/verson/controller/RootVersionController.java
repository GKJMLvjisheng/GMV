package com.cascv.oas.server.verson.controller;
/**
 * @author Ming Yang
 */
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootVersionController {
	@GetMapping("/versionManage")
	public String versionManage() {
		    return "versionManage";  
		    }
}