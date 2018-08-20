package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserWallet implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter private Integer id;
  @Getter @Setter private Integer useId;
  @Getter @Setter private BigDecimal balance;
}
