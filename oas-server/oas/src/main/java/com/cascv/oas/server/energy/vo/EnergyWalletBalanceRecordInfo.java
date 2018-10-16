package com.cascv.oas.server.energy.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ming Yang
 */
public class EnergyWalletBalanceRecordInfo {
	  @Getter @Setter private String name;
	  @Getter @Setter private BigDecimal point;
	  @Getter @Setter private BigDecimal power;
}
