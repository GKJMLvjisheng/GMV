package com.cascv.oas.server.wechat.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.server.wechat.Service.MiniProgramService;
import com.cascv.oas.server.wechat.Service.WechatService;
import com.cascv.oas.server.wechat.Utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping(value="/api/v1/miniProgram")
public class MiniProgramController{
	@Autowired
	private MiniProgramService miniProgramService;
	@GetMapping(value="/identifyHandle")
	public void linkHandle(HttpServletRequest request,HttpServletResponse response) throws IOException{
	    log.info("***EnterGet***");
  
        String signature = request.getParameter("signature");
      
        String timestamp = request.getParameter("timestamp");
        
        String nonce = request.getParameter("nonce");
       
        String echostr = request.getParameter("echostr");
        
        PrintWriter out = response.getWriter();
        
        boolean check = StringUtils.checkSignature(signature, timestamp, nonce);
        if(check){
        out.print(echostr);;
        log.info("***success***");
        }
        out.close();
        out = null;
}
 
		/**
		* @param request
		* @param response
		* @throws Exception 
		*/
		@PostMapping(value="/messageHandle")
		public Map<String,String> messageHandle(HttpServletRequest request,HttpServletResponse response) throws Exception{
			//Map<String,String> info=new HashMap<>(); 
			log.info("***接受微信服务端发来的请求***");
			request.setCharacterEncoding("UTF-8");
			response.setCharacterEncoding("UTF-8");
			log.info(String.valueOf(request.getContentLength()));
			log.info(request.getParameter("name"));			
			Map<String,String> info = miniProgramService.processRequest(request);
			//PrintWriter out=response.getWriter(); 
			//out.print(responseMessage);
			//out.flush();
			return info;
		}
        

}
