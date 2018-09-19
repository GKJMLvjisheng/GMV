package com.cascv.oas.server.user.service;


import java.util.List;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.user.mapper.UserModelMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserService {
	
  @Autowired
  private UserModelMapper userModelMapper;
	
  public UserModel findUserByName(String name){
	  //System.out.println(name);
    UserModel userModel = userModelMapper.selectByName(name); 
    return userModel;
  }

  public UserModel findUserByUuid(String uuid){
    UserModel userModel = userModelMapper.selectByUuid(uuid); 
    return userModel;
  }
  
  public UserModel findUserByInviteCode(Integer inviteCode) {
	  UserModel userModel = userModelMapper.selectByInviteCode(inviteCode);
	  return userModel;
  }
  
//  //随机产生六位邀请码，已判断重复
//  public String GenerateInviteCode() {
//	  long i = (int)((Math.random()*9+1)*10000000);
//	  String inviteCode = Long.toString(i, 36);
//	  if (findUserByInviteCode(inviteCode) == null) {
//		  return inviteCode;		  
//	  } else {
//		  return GenerateInviteCode();		  
//		  }	  
//  }
   
  public ErrorCode addUser(String uuid, UserModel userModel) {
	  String s = userModel.getName();
	  if(s == "") {
      return ErrorCode.USERNAME_NULL;
    }
	  String password = userModel.getPassword();
    if(password == "") {
      return ErrorCode.PASSWORD_NULL;
    }

    UserModel searchUserModel = findUserByName(userModel.getName());
    if (searchUserModel != null) {
      return ErrorCode.USER_ALREADY_EXISTS;
    }
    if (userModel.getNickname() == null) {
      userModel.setNickname(userModel.getName());
    }
    userModel.setUuid(uuid);
    userModel.setSalt(DateUtils.dateTimeNow());
	  userModel.setPassword(new Md5Hash(userModel.getName() + password + userModel.getSalt()).toHex().toString());
	
    //产生邀请码       
//    String inviteCode = InviteCodeUtils.getFromUuid(uuid);
//    log.info("inviteCode {}", inviteCode);
//    userModel.setInviteCode(inviteCode);
    String now = DateUtils.dateTimeNow();
    
    userModel.setCreated(now);
    userModel.setUpdated(now);
    userModelMapper.insertUser(userModel);
    log.info("inviteCode {}", userModel.getInviteCode());
    return ErrorCode.SUCCESS;
  }
  
  public ErrorCode deleteUserByUuid(String uuid) {
    userModelMapper.deleteUser(uuid);
    return ErrorCode.SUCCESS;
  }
  
	/*
	 * Name:upadateUserInfo
	 * Author:lvjisheng
	 * Date:2018.09.03
	 */
	public Integer updateUser(UserModel userModel) {
		return userModelMapper.updateUserInfo(userModel);
	}
	
	/*
	 * Name:upadateUserInfo
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	public Integer updateUserProfile(UserModel userModel) {
		return userModelMapper.updateUserProfile(userModel);
	}
	
	/*
	 * Name:resetMobile
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	public Integer resetMobileByName(UserModel userModel) {
		return userModelMapper.resetMobileByName(userModel);
	}	
	
	/*
	 * Name:resetEmail
	 * Author:lvjisheng
	 * Date:2018.09.04
	 */
	public Integer resetEmailByName(UserModel userModel) {
		return userModelMapper.resetEmailByName(userModel);
	}	
	

	public List<String> selectUserMail(String email){
		return userModelMapper.selectUserMail(email);
	}	

	public List<String> selectUserMobile(String mobile){
		return userModelMapper.selectUserMobile(mobile);
	}	
	//验证码
	public Integer updateIdentifyCode(UserModel userModel) {
		return userModelMapper.updateIdentifyCode(userModel);
	}
	
}



