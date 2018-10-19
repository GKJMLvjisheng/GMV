package com.cascv.oas.server.miner.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class SystemParameterModel {
	
	@Getter @Setter private Integer uuid;
	@Getter @Setter private BigDecimal parameterValue;
	@Getter @Setter private String parameterName;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;

}
