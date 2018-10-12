package com.cascv.oas.server.blockchain.constant;

import lombok.Getter;
import lombok.Setter;

/**
 * 提币充币事件进行状态和类型
 * @author zht
 *
 */
public enum OasEventEnum {
  
  FORSURE(0, "待管理员确认或进行中"),
  ADMIN_AGRESS(1, "管理员同意"),
  ADMIN_REJECT(2,"管理员拒绝"),
  SUCCESS(3, "成功"),
  FAILED(4,"失败"),
  
  OAS_OUT(1,"提币"),
  OAS_IN(0,"充币"),
  
  EXCHANGE_SUCCESS(1,"成功"),
  EXCHANGE_FAILED(2,"失败");
  
  @Getter @Setter private Integer code;
  @Getter @Setter private String text;

  private OasEventEnum(Integer code, String text) {
    this.code = code;
    this.text = text;
  }
}
