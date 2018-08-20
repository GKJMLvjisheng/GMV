package com.cascv.oas.server.user.vo;

import java.io.Serializable;

import com.cascv.oas.server.user.model.UserModel;

import lombok.Getter;
import lombok.Setter;

public class LoginResult implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  @Getter @Setter private String token;
  @Getter @Setter private String name;
  @Getter @Setter private String nickname;
  @Getter @Setter private String gender;
  @Getter @Setter private String address;
  @Getter @Setter private String birthday;
  @Getter @Setter private String mobile;
  @Getter @Setter private String email;
  @Getter @Setter private Integer inviteCode;
  
  public void fromUserModel(UserModel userModel) {
    this.setName(userModel.getName());
    this.setNickname(userModel.getNickname());
    this.setGender("男");
    this.setBirthday("2018-09-01");
    this.setMobile("13564537890");
    this.setEmail("owner@oases.com");
    this.setInviteCode(userModel.getId());
  }
}

