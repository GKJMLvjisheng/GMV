package com.cascv.oas.server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloWorld {
    
  @RequestMapping(value="/hello")
  public String hello(){
    return "HelloWorld";
  }
}
