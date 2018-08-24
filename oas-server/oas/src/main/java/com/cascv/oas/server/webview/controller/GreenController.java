package com.cascv.oas.server.webview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GreenController {
  
  @RequestMapping(value="/greenMap")
  public String greenMap() {
    return "index";
  }
}
