package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class MinerUpdate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String minerCode;
	@Getter @Setter private String minerName;
	@Getter @Setter private String minerDescription;
	@Getter @Setter private BigDecimal minerPrice;
	@Getter @Setter private BigDecimal minerEfficiency;
	@Getter @Setter private Integer minerPeriod;

}
