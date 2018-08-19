package com.cascv.oas.server.user.controller;

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
import com.cascv.oas.server.blockchain.model.EthHdWallet;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.response.LoginResult;
import com.cascv.oas.server.user.response.RegisterResult;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.utils.ShiroUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")
public class UserController {
	
  @Autowired
  private UserService userService;
  
  @Autowired
  private EthWalletService ethWalletService;
  
  // Login 
	@ApiOperation(value="Login", notes="")
	@PostMapping(value="/login")
	@ResponseBody
	public ResponseEntity<?> userLogin(@RequestBody UserModel userModel) {
		log.info("authentication name {}, password {}", userModel.getName(), userModel.getPassword());
		UsernamePasswordToken token = new UsernamePasswordToken(userModel.getName(), userModel.getPassword(), false);
        LoginResult loginResult = new LoginResult();
		    Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
            loginResult.setToken(ShiroUtils.getSessionId());
            loginResult.fromUserModel(ShiroUtils.getUser());
            return new ResponseEntity.Builder<LoginResult>()
                  .setData(loginResult)
                  .setStatus(ErrorCode.SUCCESS)
                  .setMessage("认证成功").build();
        } catch (AuthenticationException e)  {
            return new ResponseEntity.Builder<LoginResult>()
                  .setData(loginResult)
                  .setStatus(ErrorCode.GENERAL_ERROR)
                  .setMessage("认证失败").build();
        }
	}
	
	// Register
	@ApiOperation(value="Register", notes="")
	@PostMapping(value="/register")
	@ResponseBody
	public ResponseEntity<?> register(@RequestBody UserModel userModel) {
	  String password = userModel.getPassword();
	  log.info("register name {}, password {}", userModel.getName(), password);
		userModel.setSalt(DateUtils.dateTimeNow());
		userModel.setPassword(new Md5Hash(userModel.getName() + password + userModel.getSalt()).toHex().toString());
		String now = DateUtils.dateTimeNow();
		userModel.setCreated(now);
		userModel.setUpdated(now);
		RegisterResult registerResult = new RegisterResult();
  	Integer ret = ErrorCode.GENERAL_ERROR;
  	String msg = "用户已经存在";
  	if (userService.findUserByName(userModel.getName()) == null) {
  	  userService.addUser(userModel);
  	  EthHdWallet ethHdWallet = ethWalletService.create(userModel.getId(), password);
  	  registerResult.setMnemonicList(EthHdWallet.fromMnemonicList(ethHdWallet.getMnemonicList()));
  	  ret = ErrorCode.SUCCESS;
  	  msg = "用户注册成功";
  	}
  	return new ResponseEntity.Builder<RegisterResult>()
		      .setData(registerResult)
		      .setStatus(ret)
		      .setMessage(msg).build();
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
