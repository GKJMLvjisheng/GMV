package com.cascv.oas.server.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.mapper.UserModelMapper;

@Service
public class UserService {
	
  @Autowired
  private UserModelMapper userModelMapper;
	
  public UserModel findUserByName(String name){
    UserModel userModel = userModelMapper.selectByName(name); 
    return userModel;
  }
  
  public Integer addUser(UserModel userModel) {
    UserModel searchUser = userModelMapper.selectByName(userModel.getName());
    if (searchUser != null) {
      return 2;
    }
    if (userModel.getNickname() == null) {
      userModel.setName(userModel.getName());
    }
    return userModelMapper.insertUser(userModel);
  }
}
