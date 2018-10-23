package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class UserWalletBalanceSummary implements Serializable {

	private static final long serialVersionUID = 1L;
    @Getter @Setter private double availableBalance;
    @Getter @Setter private double ongoingBalance;
    @Getter @Setter private double availableBalanceValue;
}