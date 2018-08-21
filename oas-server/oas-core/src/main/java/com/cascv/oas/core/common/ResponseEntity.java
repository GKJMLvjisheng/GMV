package com.cascv.oas.core.common;


import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ResponseEntity<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private Integer code;
	@Getter @Setter private String message;
	@Getter @Setter private T data;
	
  public ResponseEntity() {
    this.code = 0;
    this.message = "";
  }

  public ResponseEntity(Builder<T> builder) {
      this.code = builder.getErrorCode().getCode();
      this.message = builder.getErrorCode().getMessage();
      this.data = builder.data;
  }

  public void setErrorMessage(String msg){
      this.code = 1;
      this.message = msg;
  }

  public void setErrorMessage(String msg, int code){
      this.code = code;
      this.message = msg;
  }

  public static class Builder<T>{
      @Getter private ErrorCode errorCode;
      private T data;

      public Builder() {
          this.errorCode = ErrorCode.SUCCESS;
      }

      public Builder<?> setErrorCode(ErrorCode errorCode) {
          this.errorCode = errorCode;
          return this;
      }

      public Builder<?> setData(T data) {
          this.data = data;
          return this;
      }

      public ResponseEntity<T> build(){
          return new ResponseEntity<>(this);
      }
  }
}