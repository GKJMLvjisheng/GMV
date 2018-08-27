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

    this.setGender(userModel.getGender());
    if (this.getGender() == null) {
      this.setGender("未设置");  
    }
     
    this.setBirthday(userModel.getBirthday());
    if (this.getBirthday() == null) {
      this.setBirthday("未设置");
    }
    this.setMobile(userModel.getMobile());
    if (this.getMobile() == null) {
      this.setMobile("未设置");  
    }
    this.setEmail(userModel.getEmail());
    if (this.getEmail() == null) {
      this.setEmail("未设置");  
    }
    
    this.setAddress(userModel.getAddress());
    if (this.getAddress() == null) {
      this.setAddress("未设置");  
    }
    this.setInviteCode(userModel.getInviteCode());
  }
}

