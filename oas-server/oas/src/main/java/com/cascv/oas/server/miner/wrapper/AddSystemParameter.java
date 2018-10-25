package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class AddSystemParameter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String parameterName;
	@Getter @Setter private BigDecimal parameterValue;
	@Getter @Setter private String period;

}