package com.gkyj.gmv.server.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.gkyj.gmv.server.utils.ServletUtils;

@Controller
public class RootController {
	
	@GetMapping(value="/")
	public String home() {
		return "redirect:/swagger-ui.html";
	}
	
  // Login 
  @GetMapping("/login")
  public String login(HttpServletRequest request, HttpServletResponse response) {
    if (ServletUtils.isAjaxRequest(request)){
      return ServletUtils.renderString(response, "{\"code\":\"10001\",\"message\":\"未认证\"}");
    }
    return "login";  }
  
  @GetMapping("/main")
  public String main() {
       
    return "main";  }
  @GetMapping("/table")
  public String table() {
       
    return "table";  }

  @GetMapping("/canvas")
  public String videoAndCanvas() {
       
    return "canvas";  }

  
//Line
 @GetMapping("/line")
 public String line(HttpServletRequest request, HttpServletResponse response) {
   return "line";  }
 
  @GetMapping(value="/wbTest")
  public String wbTest(){
    return "wbTest";
  }

  @GetMapping(value="/video")
  public String video(){
    return "video";
  }
  
  @GetMapping(value="/maps/maps")
  public String maps(){
    return "maps/maps";
  }
}



