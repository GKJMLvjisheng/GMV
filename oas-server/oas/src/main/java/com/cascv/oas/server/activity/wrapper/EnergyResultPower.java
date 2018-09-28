package com.cascv.oas.server.activity.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EnergyResultPower implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@Getter @Setter private BigDecimal newPower;

}
