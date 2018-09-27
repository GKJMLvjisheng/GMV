package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EthWalletTotalTradeRecordInfo implements Serializable {
  private static final long serialVersionUID = 1L;
	@Getter @Setter private String name;
    @Getter @Setter private BigDecimal value;
}
