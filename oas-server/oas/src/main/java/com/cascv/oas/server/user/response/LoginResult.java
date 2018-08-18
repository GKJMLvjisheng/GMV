package com.cascv.oas.server.user.response;

import java.io.Serializable;

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
}

