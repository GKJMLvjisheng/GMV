package com.cascv.oas.server.blockchain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EthWalletSummary implements Serializable {
  private static final long serialVersionUID = 1L;
  @Setter @Getter private Integer totalValue;
}
