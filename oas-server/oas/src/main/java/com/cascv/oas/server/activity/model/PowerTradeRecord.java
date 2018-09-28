package com.cascv.oas.server.activity.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class PowerTradeRecord {
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private String energyBalluuid;
	@Getter @Setter private Integer inOrOut;
	@Getter @Setter private BigDecimal powerChange;
	@Getter @Setter private Integer status;
	@Getter @Setter private String created;

}
