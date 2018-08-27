package com.cascv.oas.server.blockchain.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserWalletDetail {
  @Getter @Setter private String uuid;
  @Getter @Setter private String userUuid;
  @Getter @Setter private BigDecimal value;
  @Getter @Setter private String scope;
  @Getter @Setter private String name;
  @Getter @Setter private String comment;
  @Getter @Setter private String created;
  
}
