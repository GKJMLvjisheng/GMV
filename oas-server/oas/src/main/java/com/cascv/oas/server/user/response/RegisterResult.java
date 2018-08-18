package com.cascv.oas.server.user.response;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class RegisterResult implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter List<String> mnemonicList;
}
