package com.cascv.oas.server.activity.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EnergyResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private BigDecimal newPoint;
	@Getter @Setter private BigDecimal newPower;

}
