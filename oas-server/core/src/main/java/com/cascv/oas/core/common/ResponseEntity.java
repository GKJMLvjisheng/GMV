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
      this.code = builder.code;
      this.message = builder.message;
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
      private Integer code;
      private String message;
      private T data;

      public Builder() {
          this.code= 0;
          this.message = "";
      }

      public Builder<?> setStatus(Integer code) {
          this.code = code;
          return this;
      }

      public Builder<?> setMessage(String message) {
          this.message = message;
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