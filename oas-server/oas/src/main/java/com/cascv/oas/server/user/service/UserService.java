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
    UserModel userModel = userModelMapper.selectByName(name); 
    return userModel;
  }

  public UserModel findUserByUuid(String uuid){
    UserModel userModel = userModelMapper.selectByUuid(uuid); 
    return userModel;
  }
  
  
  public Integer addUser(UserModel userModel) {
    UserModel searchUserModel = findUserByName(userModel.getName());
    if (searchUserModel != null)
      return ErrorCode.USER_ALREADY_EXISTS;
    if (userModel.getNickname() == null) {
      userModel.setNickname(userModel.getName());
    }
    
    String password = userModel.getPassword();
    userModel.setPassword(new Md5Hash(userModel.getName() + password + userModel.getSalt()).toHex().toString());
    
    String now = DateUtils.dateTimeNow();
    userModel.setUuid(UUIDUtils.getUUID());
    userModel.setCreated(now);
    userModel.setUpdated(now);
    userModel.setSalt(DateUtils.dateTimeNow());
    userModelMapper.insertUser(userModel);
    return ErrorCode.SUCCESS;
  }
  
  public Integer deleteUserById(Integer id) {
    userModelMapper.deleteUser(id);
    return ErrorCode.SUCCESS;
  }
  
}
