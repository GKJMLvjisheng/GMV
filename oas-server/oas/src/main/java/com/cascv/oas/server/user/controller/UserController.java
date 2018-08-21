package com.cascv.oas.server.user.controller;

<<<<<<< HEAD
import javax.servlet.http.HttpServletRequest;
=======
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
>>>>>>> bcc2edfdcdcdc802837221d968513a9b42985f00

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
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
import com.cascv.oas.server.utils.ServletUtils;
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
  @ApiOperation(value="user login", notes="")
  @GetMapping("/login")
  public String login(HttpServletRequest request, HttpServletResponse response) {
    if (ServletUtils.isAjaxRequest(request)){
      return ServletUtils.renderString(response, "{\"code\":\"1\",\"message\":\"未认证\"}");
    }
    return "login";
  }
  
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
	@Transactional
	public ResponseEntity<?> register(@RequestBody UserModel userModel) {

	  String password = userModel.getPassword();
	  log.info("register name {}, password {}", userModel.getName(), password);
		RegisterResult registerResult = new RegisterResult();
  	
		String msg = "用户已经存在";
		Integer ret = userService.addUser(userModel);
  	if (ret == ErrorCode.SUCCESS) {
  	  EthHdWallet ethHdWallet = ethWalletService.create(userModel.getId(), password);
  	  registerResult.setMnemonicList(EthHdWallet.fromMnemonicList(ethHdWallet.getMnemonicList()));
  	  registerResult.setUuid(userModel.getUuid());
  	  ret = ErrorCode.SUCCESS;
  	  msg = "用户注册成功";
  	}
  	return new ResponseEntity.Builder<RegisterResult>()
		      .setData(registerResult)
		      .setStatus(ret)
		      .setMessage(msg).build();
	}

  @PostMapping(value="/registerConfirm")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> registerConfirm(@RequestBody RegisterConfirm registerConfirm) {
    UserModel userModel = userService.findUserByUuid(registerConfirm.getUuid());
    if (userModel != null && registerConfirm.getCode() != ErrorCode.SUCCESS) {
      userService.deleteUserById(userModel.getId());
    }
    return new ResponseEntity.Builder<Integer>().setData(0).setStatus(0).setMessage("成功").build();
	}
	
	
	// inquireName
	@PostMapping(value="/inquireName")
	@ResponseBody
	public ResponseEntity<?> inquireName(HttpServletRequest request) {
		
		String name = request.getParameter("name");
		
		System.out.println(name);
		
		System.out.println(userService.findUserByName(name));
		
	  if (userService.findUserByName(name) == null) {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0)
		        .setStatus(ErrorCode.GENERAL_ERROR)
<<<<<<< HEAD
		        .setMessage("no").build(); 
=======
		        .setMessage("已使用").build(); 
>>>>>>> bcc2edfdcdcdc802837221d968513a9b42985f00
	  } else {
		  return new ResponseEntity.Builder<Integer>()
		        .setData(0)
		        .setStatus(ErrorCode.SUCCESS)
		        .setMessage("未使用").build();
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
	      .setData(info).setStatus(ErrorCode.SUCCESS).setMessage("成功").build();
	}
}
