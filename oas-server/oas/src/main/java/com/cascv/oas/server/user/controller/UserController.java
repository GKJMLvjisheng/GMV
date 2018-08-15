package com.cascv.oas.server.user.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.core.utils.StringUtils;
import com.cascv.oas.server.utils.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value="User Interface")
@RequestMapping(value="/userCenter")
public class UserController {
	
  @Autowired
  private UserService userService;
  
	@ApiOperation(value="", notes="")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "user name", required = true, dataType = "String"),
        @ApiImplicitParam(name = "password", value = "user password", required = true, dataType = "String")
	})
	@RequestMapping(value="/login", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> userLogin(@RequestBody UserModel userModel,HttpServletResponse response) {
		log.info("authentication name {}, password {}", userModel.getName(), userModel.getPassword());
		UsernamePasswordToken token = new UsernamePasswordToken(userModel.getName(), userModel.getPassword(), false);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            response.addHeader("token", ShiroUtils.getSessionId());
            return new ResponseEntity.Builder<Integer>().setData(0).setStatus(0).setMessage("ok").build();
        } catch (AuthenticationException e)  {
            String msg = "xxxx";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return new ResponseEntity.Builder<Integer>().setData(0).setStatus(1).setMessage(msg).build();
        }
	}
	
	@ApiOperation(value="user register", notes="")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "user name", required = true, dataType = "String"),
        @ApiImplicitParam(name = "password", value = "user password", required = true, dataType = "String")
	})
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<?> register(@RequestBody UserModel userModel) {
		log.info("register name {}, password {}", userModel.getName(), userModel.getPassword());
		
		userModel.setSalt(Integer.toHexString((int)(Math.random() * 10)));
		String password = new Md5Hash(userModel.getName() + userModel.getPassword() + userModel.getSalt()).toHex().toString();
		userModel.setPassword(password);
		Integer ret = userService.addUser(userModel);
		return new ResponseEntity.Builder<Integer>().setData(0).setStatus(ret).setMessage("complete").build();
	}

	@RequestMapping(value="/inquireName", method=RequestMethod.POST)
	public ResponseEntity<?> inquireName(String name) {
	  return new ResponseEntity.Builder<Integer>().setData(0).setStatus(0).setMessage("ok").build();
	}
}
