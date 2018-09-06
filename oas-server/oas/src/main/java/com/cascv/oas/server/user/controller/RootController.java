package com.cascv.oas.server.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cascv.oas.server.utils.ServletUtils;

import io.swagger.models.Model;

@Controller
public class RootController {
	
	@GetMapping(value="/")
	public String home() {
		return "redirect:/swagger-ui.html";
	}
	
	@RequestMapping(value="/userImgInfo")
	public String userImgInfo(Model model) {
		
	    return "userImgInfo";
	}
	
	@GetMapping(value="/userInfo/userInfo")
	public String userInfo() {
		
	    return "userInfo/userInfo";
	}
	
	@RequestMapping(value="/userSecurityInfo")
	public String userSecurityInfo(Model model) {
		
	    return "userSecurityInfo";
	}
	
	//身份验证主页
    @RequestMapping(value="/SecurityStep1")
    public String SecurityStep1(Model model) {
      
          return "SecurityStep1";
    }
    
    //修改邮箱/手机成功主页
    @RequestMapping(value="/resetMobileSuccess")
    public String resetMobileSuccess(Model model) {
      
          return "resetMobileSuccess";
    }
    //修改邮箱主页
    @RequestMapping(value="/resetMail")
	  public String resetMail(Model model) {
	    
	        return "/resetMail";
	  }
    
    //修改手机主页
    @RequestMapping(value="/resetMobile")
    public String resetMobile(Model model) {
      
          return "resetMobile";
    }
    
    //修改邮箱主页
    @RequestMapping(value="/resetMailLink")
    public String resetMailLink(Model model) {
      
          return "resetMailLink";
    }
    
   
	
    // Login 
	  @GetMapping("/login")
	  public String login(HttpServletRequest request, HttpServletResponse response) {
	    if (ServletUtils.isAjaxRequest(request)){
	      return ServletUtils.renderString(response, "{\"code\":\"10001\",\"message\":\"未认证\"}");
	   }
	      return "login";  }
  
	  @GetMapping(value="/main")
	  public String main() {
	    
	        return "main";
  }
   }

