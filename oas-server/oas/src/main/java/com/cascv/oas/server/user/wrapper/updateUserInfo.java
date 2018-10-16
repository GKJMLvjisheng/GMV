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
  @Getter @Setter String nickname;
  @Getter @Setter String name;
  @Getter @Setter String gender;
  @Getter @Setter String birthday;
  @Getter @Setter String address;
}