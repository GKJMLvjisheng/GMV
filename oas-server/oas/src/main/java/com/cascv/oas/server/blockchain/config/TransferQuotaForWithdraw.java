package com.cascv.oas.server.blockchain.config;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class TransferQuotaForWithdraw  implements Serializable {
	 private static final long serialVersionUID = 1L;
	  @Getter @Setter private String toUserAddress;
	  @Getter @Setter private BigDecimal amount;
	  @Getter @Setter private String remark;
	  @Getter @Setter private String oasDetailUuid;
}
