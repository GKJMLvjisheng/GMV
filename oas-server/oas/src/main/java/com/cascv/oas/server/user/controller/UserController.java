package com.cascv.oas.server.user.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.StringUtils;
import com.cascv.oas.server.utils.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value="User Interface")
@RequestMapping(value="/userCenter")
public class UserController {
	
  @Autowired
  private UserService userService;
  
  // Login 
	@ApiOperation(value="Login", notes="")
	@PostMapping(value="/login")
	@ResponseBody
	public ResponseEntity<?> userLogin(@RequestBody UserModel userModel,HttpServletResponse response) {
		log.info("authentication name {}, password {}", userModel.getName(), userModel.getPassword());
		UsernamePasswordToken token = new UsernamePasswordToken(userModel.getName(), userModel.getPassword(), false);
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            response.addHeader("token", ShiroUtils.getSessionId());
            return new ResponseEntity.Builder<Integer>()
                  .setData(0)
                  .setStatus(ErrorCode.SUCCESS)
                  .setMessage("ok").build();
        } catch (AuthenticationException e)  {
            String msg = "xxxx";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return new ResponseEntity.Builder<Integer>()
                  .setData(0)
                  .setStatus(ErrorCode.GENERAL_ERROR)
                  .setMessage(msg).build();
        }
	}
	
	// Register
	@ApiOperation(value="Register", notes="")
	@PostMapping(value="/register")
	@ResponseBody
	public ResponseEntity<?> register(@RequestBody UserModel userModel) {
		
	  log.info("register name {}, password {}", userModel.getName(), userModel.getPassword());
		
		userModel.setSalt(DateUtils.YYYYMMDDHHMMSS);
		String password = new Md5Hash(userModel.getName() + userModel.getPassword() + userModel.getSalt()).toHex().toString();
		userModel.setPassword(password);
		String now = DateUtils.dateTimeNow();
		userModel.setCreated(now);
		userModel.setUpdated(now);
		
		Integer ret = ErrorCode.GENERAL_ERROR;
		if (userService.addUser(userModel) > 0) {
			ret = ErrorCode.SUCCESS;
		}
		return new ResponseEntity.Builder<Integer>()
		      .setData(0)
		      .setStatus(ret)
		      .setMessage("complete").build();
	}

	// inquireName
	@PostMapping(value="/inquireName")
	@ResponseBody
	public ResponseEntity<?> inquireName(String name) {
	  if (userService.findUserByName(name) == null) {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0)
		        .setStatus(ErrorCode.GENERAL_ERROR)
		        .setMessage("ok").build(); 
	  } else {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0)
		        .setStatus(ErrorCode.SUCCESS)
		        .setMessage("ok").build();
	  }
	}
}
