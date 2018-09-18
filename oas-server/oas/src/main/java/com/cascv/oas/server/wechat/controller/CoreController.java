package com.cascv.oas.server.wechat.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Param;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.server.energy.controller.EnergyPowerController;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.wechat.Service.WechatService;
import com.cascv.oas.server.wechat.Utils.StringUtils;
import com.cascv.oas.server.wechat.vo.IdenCodeDomain;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
@Slf4j
@RestController
@RequestMapping(value="/api/v1/wx")
public class CoreController{
	@Autowired
	private WechatService wechatService;
	@GetMapping(value="/handle")
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
@PostMapping(value="/handle")
public void messageHandle(HttpServletRequest request,HttpServletResponse response) throws Exception{
	log.info("***接受微信服务端发来的请求***");
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	log.info(String.valueOf(request.getContentLength()));
	String responseMessage = wechatService.processRequest(request);
	PrintWriter out=response.getWriter(); 
	out.print(responseMessage);
	out.flush();
}
@PostMapping(value="/checkIdenCode")
public String messageHandle(@RequestBody IdenCodeDomain code){
   Map<String,Object> userInfo=wechatService.inquireUserInfo();
   String idenCode=code.getIdenCode();
   log.info(String.valueOf(userInfo));
   String name= ShiroUtils.getUser().getName();
   log.info(userInfo.get(name).toString());
   log.info(idenCode);
   if(userInfo.get(name).equals(idenCode)){
	   log.info("验证成功,提升算力！");
	   return "success";
   }
   else {
	   log.info("验证失败！");
	   return "failure";
        }
   }
}

