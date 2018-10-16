package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserWalletTransfer implements Serializable {
  private static final long serialVersionUID = 1L;
	@Getter @Setter private String toUserName;
    @Getter @Setter private BigDecimal value;
    @Getter @Setter private String remark;
    @Getter @Setter private String changeUserName;
    @Getter @Setter private String comment;
}
