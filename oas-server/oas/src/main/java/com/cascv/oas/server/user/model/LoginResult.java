package com.cascv.oas.server.user.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class LoginResult implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter String token;
}
