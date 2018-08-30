package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EthWalletSummary implements Serializable {
  private static final long serialVersionUID = 1L;
  @Setter @Getter private BigDecimal totalBalance;
  @Setter @Getter private BigDecimal totalValue;
  @Setter @Getter private BigDecimal totalTransaction;
}
