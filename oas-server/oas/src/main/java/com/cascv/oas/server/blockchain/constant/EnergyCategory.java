package com.cascv.oas.server.blockchain.constant;

import lombok.Getter;
import lombok.Setter;

public enum EnergyCategory {
  PHONE(0, "手机"),
  WATCH(1, "手表"),
  EQUIPMENT(2, "家电");
  
  @Getter @Setter private Integer code;
  @Getter @Setter private String text;

  private EnergyCategory(Integer code, String text) {
    this.code = code;
    this.text = text;
  }
}
