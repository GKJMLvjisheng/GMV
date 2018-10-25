package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class SystemParameterResponse implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private BigDecimal parameterValue;
	@Getter @Setter private String period;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;
	@Getter @Setter private String comment;
	@Getter @Setter private Integer currency;
	@Getter @Setter private String parameterName;

}
