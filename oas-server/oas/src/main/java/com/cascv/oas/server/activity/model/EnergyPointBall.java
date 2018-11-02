package com.cascv.oas.server.activity.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EnergyPointBall {
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private String sourceUuid;
	@Getter @Setter private BigDecimal point;
	@Getter @Setter private Integer status;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;

}
