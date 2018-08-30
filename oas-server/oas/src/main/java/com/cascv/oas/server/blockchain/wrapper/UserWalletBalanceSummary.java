package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserWalletBalanceSummary implements Serializable {

	private static final long serialVersionUID = 1L;
    @Getter @Setter private BigDecimal availableBalance;
    @Getter @Setter private BigDecimal ongoingBalance;
    @Getter @Setter private BigDecimal availableBalanceValue;
}