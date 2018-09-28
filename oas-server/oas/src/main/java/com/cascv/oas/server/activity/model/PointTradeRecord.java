package com.cascv.oas.server.activity.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class PointTradeRecord {
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private String energyBallUuid;
	@Getter @Setter private Integer inOrOut;
	@Getter @Setter private BigDecimal pointChange;
	@Getter @Setter private Integer status;
	@Getter @Setter private String created;

}
