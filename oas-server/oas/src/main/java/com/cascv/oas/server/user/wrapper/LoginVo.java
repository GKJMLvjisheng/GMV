package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class LoginVo implements Serializable{

  private static final long serialVersionUID = 1L;
  @Setter @Getter private String name;
  @Setter @Getter private String password;
  @Setter @Getter private Boolean rememberMe;
  @Setter @Getter private Integer control;
}
