package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EthWalletTradeRecordInfo implements Serializable {
  private static final long serialVersionUID = 1L;
	@Getter @Setter private String name;
    @Getter @Setter private BigDecimal value;
    @Getter @Setter private String title;
    @Getter @Setter private String subTitle;
    @Getter @Setter private String remark;
    @Getter @Setter private String created;
}
