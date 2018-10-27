package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class PowerTradeRecordDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String name;
	@Getter @Setter private BigDecimal powerChange;
	@Getter @Setter private String sourceName;
	@Getter @Setter private Integer inOrOut;
	@Getter @Setter private String created;
}
