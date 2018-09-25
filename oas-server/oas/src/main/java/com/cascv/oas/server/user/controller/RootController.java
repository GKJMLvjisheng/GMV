package com.cascv.oas.server.user.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.cascv.oas.server.utils.ServletUtils;
@Controller
public class RootController {
		
	@GetMapping(value="/")
	public String home() {
		return "redirect:/swagger-ui.html";
	}
	
	@GetMapping(value="/userInfo/userImgInfo")
	public String userImgInfo(){
		
	    return "userInfo/userImgInfo";
	}
	
	@GetMapping(value="/userInfo/userInfo")
	public String userInfo() {
		
	    return "userInfo/userInfo";
	}
	
	@GetMapping(value="/userInfo/userSecurityInfo")
	public String userSecurityInfo() {
		
	    return "userInfo/userSecurityInfo";
	}
	
	//身份验证主页
    @GetMapping(value="/userInfo/SecurityStep1")
    public String SecurityStep1() {
      
          return "userInfo/SecurityStep1";
    }
    
//    //修改邮箱/手机成功主页
//    @GetMapping(value="/userInfo/resetMail/resetMMSuccess")
//    public String resetMMSuccess() {
//      
//          return "userInfo/restMail/resetMMSuccess";
//    }
    
    //修改邮箱/手机成功主页
    @GetMapping(value="/userInfo/resetMMSuccess")
    public String resetMMSuccess() {
    
        return "userInfo/resetMMSuccess";
    }
  
    //修改邮箱主页
    @GetMapping(value="/userInfo/resetMail/resetMail")
	  public String resetMail() {
	    
	        return "userInfo/resetMail/resetMail";
	  }
    
    //修改手机主页
    @GetMapping(value="/userInfo/resetMobile/resetMobile")
    public String resetMobile() {
      return "userInfo/resetMobile/resetMobile";
    }

    // Login 
	  @GetMapping("/login")
	  public String login(HttpServletRequest request, HttpServletResponse response) {
	    if (ServletUtils.isAjaxRequest(request)){
	      return ServletUtils.renderString(response, "{\"code\":\"10001\",\"message\":\"未认证\"}");
	   }
	      return "login";  }
  
	  @GetMapping(value="/main")
	  public String main(){
	    
	        return "main";
     }
	  

}

