package com.cascv.oas.server.blockchain.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserWalletDetail {
  @Getter @Setter private Integer id;
  @Getter @Setter private Integer userWalletId;
  @Getter @Setter private BigDecimal value;
  @Getter @Setter private String datetime;
  @Getter @Setter private String name;
}
