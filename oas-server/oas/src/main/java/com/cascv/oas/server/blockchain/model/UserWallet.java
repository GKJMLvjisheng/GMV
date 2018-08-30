package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserWallet implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String uuid;
  @Getter @Setter private String userUuid;
  @Getter @Setter private BigDecimal balance;
  @Getter @Setter private String created;
  @Getter @Setter private String updated;
}
