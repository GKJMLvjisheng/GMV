package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EfficiencyWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private BigDecimal minerEfficiency;

}
