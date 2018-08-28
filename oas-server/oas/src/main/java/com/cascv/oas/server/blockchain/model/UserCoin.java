package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserCoin implements Serializable{
  private static final long serialVersionUID = 1L;
  @Setter @Getter private String userUuid;
  @Setter @Getter private String contract;
  @Setter @Getter private BigDecimal weiFactor;
  @Setter @Getter private String address;
  @Setter @Getter private String name;
  @Setter @Getter private String symbol;
  @Setter @Getter private BigDecimal balance;
  @Setter @Getter private BigDecimal value;
}
