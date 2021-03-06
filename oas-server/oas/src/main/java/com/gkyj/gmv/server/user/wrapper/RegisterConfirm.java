package com.gkyj.gmv.server.user.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class RegisterConfirm implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String uuid;
  @Getter @Setter private Integer code;
}
