package com.cascv.oas.server.user.service;


import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UUIDUtils;
import com.cascv.oas.server.user.mapper.UserModelMapper;

@Service
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
  
  
  public Integer addUser(UserModel userModel) {
	  String s = userModel.getName();
	  System.out.println(s + "1234");
	  if(s == "")
		  return ErrorCode.USERNAME_NULL;
	  else {
	    UserModel searchUserModel = findUserByName(userModel.getName());
	    if (searchUserModel != null)
	      return ErrorCode.USER_ALREADY_EXISTS;
	    if (userModel.getNickname() == null) {
	      userModel.setNickname(userModel.getName());}
	  }
    
    String password = userModel.getPassword();
    if(password == "")
    	return ErrorCode.PASSWORD_NULL;
    else {
	    userModel.setSalt(DateUtils.dateTimeNow());
	    userModel.setPassword(new Md5Hash(userModel.getName() + password + userModel.getSalt()).toHex().toString());}
	
    //随机产生6位邀请码，没有判断是否重复
    long i = (int)((Math.random()*9+1)*10000000);
    String inviteCode = Long.toString(i, 36);
	  userModel.setInviteCode(inviteCode);
    
    String now = DateUtils.dateTimeNow();
    userModel.setUuid(UUIDUtils.getUUID());
    userModel.setCreated(now);
    userModel.setUpdated(now);
    userModelMapper.insertUser(userModel);
    return ErrorCode.SUCCESS;
  }
  
  public Integer deleteUserById(Integer id) {
    userModelMapper.deleteUser(id);
    return ErrorCode.SUCCESS;
  }
  
}
