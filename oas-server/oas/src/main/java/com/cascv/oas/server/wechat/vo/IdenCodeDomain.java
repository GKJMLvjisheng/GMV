package com.cascv.oas.server.wechat.vo;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class IdenCodeDomain  implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String idenCode;
}