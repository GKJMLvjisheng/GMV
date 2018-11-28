package com.gkyj.gmv.server.webview.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/api/v1")
public class GreenController {
  
  @RequestMapping(value="/greenMap")
  public String greenMap() {
    return "index";
  }
}
