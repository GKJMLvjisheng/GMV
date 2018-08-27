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
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.service.EnergyPointService;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.user.vo.LoginResult;
import com.cascv.oas.server.user.vo.LoginVo;
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
	
	@Autowired
	private UserWalletService userWalletService;
	
	@Autowired
	private EnergyPointService energyPointService;
  
	@ApiOperation(value="Login", notes="")
	@PostMapping(value="/login")
	@ResponseBody
	public ResponseEntity<?> userLogin(@RequestBody LoginVo loginVo) {
		log.info("authentication name {}, password {}", loginVo.getName(), loginVo.getPassword());
		Boolean rememberMe = loginVo.getRememberMe() == null ? false : loginVo.getRememberMe();
		UsernamePasswordToken token = new UsernamePasswordToken(loginVo.getName(), loginVo.getPassword(), rememberMe);
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
		RegisterResult registerResult = new RegisterResult();
		String uuid = UuidUtils.getPrefixUUID(UuidPrefix.USER_MODEL);
		EthWallet ethHdWallet = ethWalletService.create(uuid, password);
		userWalletService.create(uuid);
		energyPointService.create(uuid);
		ErrorCode ret = userService.addUser(uuid, userModel);
		log.info("inviteCode {}", userModel.getInviteCode());
  	if (ret.getCode() == ErrorCode.SUCCESS.getCode()) {
  	  registerResult.setMnemonicList(EthWallet.fromMnemonicList(ethHdWallet.getMnemonicList()));
  	  registerResult.setUuid(userModel.getUuid());
  	  ret = ErrorCode.SUCCESS;
  	}
  	return new ResponseEntity.Builder<RegisterResult>()
		      .setData(registerResult).setErrorCode(ret).build();
	}

	// Destroy
	@ApiOperation(value="Destroy", notes="")
	@PostMapping(value="/destroy")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> destroy() {
		UserModel userModel = ShiroUtils.getUser();
		String uuid = userModel.getUuid();
		ethWalletService.destroy(uuid);
		userWalletService.destroy(uuid);
		energyPointService.destroy(uuid);
		userService.deleteUserByUuid(uuid);
		return new ResponseEntity.Builder<Integer>()
					.setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	}

  @PostMapping(value="/registerConfirm")
  @ResponseBody
//  @Transactional
  public ResponseEntity<?> registerConfirm(@RequestBody RegisterConfirm registerConfirm) {
	  UserModel userModel = userService.findUserByUuid(registerConfirm.getUuid());
    if (userModel != null && registerConfirm.getCode() != null && registerConfirm.getCode() != 0) {
			String uuid = userModel.getUuid();
			System.out.println(uuid);
			ethWalletService.destroy(uuid);
			userWalletService.destroy(uuid);
			energyPointService.destroy(uuid);
      userService.deleteUserByUuid(uuid);
    }
    return new ResponseEntity.Builder<Integer>()
          .setData(0)
          .setErrorCode(ErrorCode.SUCCESS)
          .build();
	}
	
	
	// inquireName
	@PostMapping(value="/inquireName")
	@ResponseBody
	public ResponseEntity<?> inquireName(@RequestBody UserModel userModel) {
		
		String name = userModel.getName();
		
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
	
	
	
	@PostMapping(value="/inquireUserInfo")
	@ResponseBody
	public ResponseEntity<?> inquireUserInfo(){
	  Map<String, String> info = new HashMap<>();
		UserModel userModel = ShiroUtils.getUser();
		log.info("invideCode {}", userModel.getInviteCode());
	  info.put("name", userModel.getName());
	  info.put("nickname", userModel.getNickname());
	  info.put("inviteCode", userModel.getInviteCode().toString());
	  info.put("gender", userModel.getGender());
	  info.put("address", userModel.getAddress());
	  info.put("birthday", userModel.getBirthday());
	  info.put("email", userModel.getEmail());
	  info.put("mobile", userModel.getMobile());
	  return new ResponseEntity.Builder<Map<String, String>>()
	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
	}
}
