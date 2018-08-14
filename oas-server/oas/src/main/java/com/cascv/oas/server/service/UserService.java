package com.cascv.oas.server.service;

import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.cascv.oas.core.model.UserModel;

@Service
public class UserService {
  public UserModel findUserByName(String name){
    UserModel userModel = new UserModel();
    userModel.setName(name);;
    userModel.setNickname(name+"NICK");
    userModel.setPassword("29c605bf7f42e96b72d803cb34e2562a");//密码明文是123456
    userModel.setSalt("wxKYXuTPST5SG0jMQzVPsg==");//加密密码的盐值
    userModel.setId(new Random().nextLong());//随机分配一个id
    userModel.setCreated(new Date());
    return userModel;
  }
}
