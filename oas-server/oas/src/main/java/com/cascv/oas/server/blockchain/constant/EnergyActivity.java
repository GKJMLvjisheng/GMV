package com.cascv.oas.server.blockchain.constant;

import lombok.Getter;
import lombok.Setter;

public enum EnergyActivity{

  CHECKIN(0, "签到"),
  WALK(1, "记步");

  @Getter @Setter private Integer code;
  @Getter @Setter private String text;


  private EnergyActivity(Integer code, String text) {
    this.code = code;
    this.text = text;
  }
}