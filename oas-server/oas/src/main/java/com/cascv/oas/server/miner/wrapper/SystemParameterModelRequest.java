package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class SystemParameterModelRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private Integer uuid;
	@Getter @Setter private BigDecimal parameterValue;

}
