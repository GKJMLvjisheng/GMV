package com.cascv.oas.server.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.model.UserModel;
import com.cascv.oas.core.utils.StringUtils;
import com.cascv.oas.server.utils.ShiroUtils;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class UserController {
	
	@ApiOperation(value="用户登录", notes="")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "用户名", required = true, dataType = "String"),
        @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String")
	})
	@RequestMapping(value="/userCenter/login", method=RequestMethod.POST, consumes="application/json", produces="application/json")
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
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            }
            return new ResponseEntity.Builder<Integer>().setData(0).setStatus(1).setMessage(msg).build();
        }
	}
	@ApiOperation(value="用户注册", notes="")
	@ApiImplicitParams({
        @ApiImplicitParam(name = "name", value = "用户名", required = true, dataType = "String"),
        @ApiImplicitParam(name = "password", value = "用户密码", required = true, dataType = "String")
	})
	@RequestMapping(value="/userCenter/register", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	@ResponseBody
	public ResponseEntity<?> register(@RequestBody UserModel userModel) {
		log.info("register name {}, password {}", userModel.getName(), userModel.getPassword());
		return new ResponseEntity.Builder<Integer>().setData(0).setStatus(0).setMessage("ok").build();
	}
}
