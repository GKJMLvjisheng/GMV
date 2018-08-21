package com.cascv.oas.server.user.controller;


import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.user.vo.LoginResult;
import com.cascv.oas.server.user.vo.RegisterConfirm;
import com.cascv.oas.server.user.vo.RegisterResult;
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
                .setData(loginResult).setErrorCode(ErrorCode.SUCCESS)
                .build();
        } catch (AuthenticationException e)  {
            return new ResponseEntity.Builder<LoginResult>()
                  .setData(loginResult)
                  .setErrorCode(ErrorCode.GENERAL_ERROR).build();
        }
	}
	
	// Register
	@ApiOperation(value="Register", notes="")
	@PostMapping(value="/register")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> register(@RequestBody UserModel userModel) {

	  String password = userModel.getPassword();
	  log.info("register name {}, password {}", userModel.getName(), password);
		RegisterResult registerResult = new RegisterResult();
		ErrorCode ret = userService.addUser(userModel);
  	if (ret.getCode() == ErrorCode.SUCCESS.getCode()) {
  	  EthHdWallet ethHdWallet = ethWalletService.create(userModel.getId(), password);
  	  registerResult.setMnemonicList(EthHdWallet.fromMnemonicList(ethHdWallet.getMnemonicList()));
  	  registerResult.setUuid(userModel.getUuid());
  	  ret = ErrorCode.SUCCESS;
  	} else {
  	  ret = ErrorCode.GENERAL_ERROR;
  	}
  	return new ResponseEntity.Builder<RegisterResult>()
		      .setData(registerResult).setErrorCode(ret).build();
	}

  @PostMapping(value="/registerConfirm")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> registerConfirm(@RequestBody RegisterConfirm registerConfirm) {
    UserModel userModel = userService.findUserByUuid(registerConfirm.getUuid());
    if (userModel != null && registerConfirm.getCode() != 0) {
      userService.deleteUserById(userModel.getId());
    }
    return new ResponseEntity.Builder<Integer>().setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	}
	
	
	// inquireName
	@PostMapping(value="/inquireName")
	@ResponseBody
	public ResponseEntity<?> inquireName(@RequestBody UserModel userModel) {
		
		String name = userModel.getName();
		
		System.out.println(name);
		
	  if (userService.findUserByName(name) == null) {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0)
		        .setErrorCode(ErrorCode.SUCCESS).build();
	  } else {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0)
		        .setErrorCode(ErrorCode.GENERAL_ERROR).build();
		  }
	}
	public ResponseEntity<?> inquireName(String name) {
		
	  if (userService.findUserByName(name) == null) {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0).setErrorCode(ErrorCode.GENERAL_ERROR).build();
	  } else {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	  }
	}
	
	@PostMapping(value="/inquireUserInfo")
	@ResponseBody
	public ResponseEntity<?> inquireUserInfo(){
	  Map<String, String> info = new HashMap<>();
	  UserModel userModel = ShiroUtils.getUser();
	  info.put("name", userModel.getName());
	  info.put("nickname", userModel.getNickname());
	  info.put("inviteCode", userModel.getInviteCode());
	  info.put("gender", userModel.getGender());
	  info.put("address", userModel.getAddress());
	  info.put("birthday", userModel.getBirthday());
	  info.put("email", userModel.getEmail());
	  info.put("mobile", userModel.getMobile());
	  return new ResponseEntity.Builder<Map<String, String>>()
	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	}
}
