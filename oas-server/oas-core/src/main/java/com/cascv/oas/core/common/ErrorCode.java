package com.cascv.oas.core.common;

import lombok.Getter;
import lombok.Setter;

//abstract public class ErrorCode {
//  public static final int SUCCESS = 0;
//  public static final int GENERAL_ERROR = 10001;
//  public static final int USER_ALREADY_EXISTS = 10002;
//  public static final int USERNAME_NULL = 10003;
//  public static final int PASSWORD_NULL = 10004;
//}


public enum ErrorCode {
  SUCCESS(0, "成功"),
  GENERAL_ERROR(10001, "错误"),
  USER_ALREADY_EXISTS(10002, "用户已存在"),
  USERNAME_NULL(10003, "用户名空"),
  PASSWORD_NULL(10004, "密码空");
  
  @Getter @Setter private Integer code;
  @Getter @Setter private String message;

  private ErrorCode(Integer code, String message) {
      this.code = code;
      this.message = message;
  }
}
