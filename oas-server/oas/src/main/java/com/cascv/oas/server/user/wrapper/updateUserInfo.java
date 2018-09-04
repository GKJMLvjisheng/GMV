package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/*
 * Name:upadateUserInfo
 * Author:lvjisheng
 * Date:2018.09.03
 */	 
public class updateUserInfo implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter String userNickname;
  @Getter @Setter String userName;
  @Getter @Setter String userGender;
  @Getter @Setter String userBirthday;
  @Getter @Setter String userAddress;
  void toSring(){	  
  }
}