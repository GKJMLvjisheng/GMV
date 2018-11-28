package com.gkyj.gmv.server.user.controller;

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
import com.gkyj.gmv.server.user.model.UserModel;
import com.gkyj.gmv.server.user.service.UserService;
import com.gkyj.gmv.server.user.wrapper.LoginResult;
import com.gkyj.gmv.server.user.wrapper.LoginVo;
import com.gkyj.gmv.server.utils.ShiroUtils;
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
	


	// Destroy
	@ApiOperation(value="Destroy", notes="")
	@PostMapping(value="/destroy")
	@ResponseBody
	@Transactional
	public ResponseEntity<?> destroy() {
		UserModel userModel = ShiroUtils.getUser();
		String uuid = userModel.getUuid();
		userService.deleteUserByUuid(uuid);
		return new ResponseEntity.Builder<Integer>()
					.setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	}
		
}
