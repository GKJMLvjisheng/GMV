package com.cascv.oas.server.activity.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class ActivityModel {
	
	@Getter @Setter private Integer sourceCode;
	@Getter @Setter private String sourceName;
	@Getter @Setter private String type;
	@Getter @Setter private BigDecimal pointSingle;
	@Getter @Setter private BigDecimal powerSingle;
	@Getter @Setter BigDecimal pointIncreaseSpeed;
	@Getter @Setter BigDecimal powerIncreaseSpeed;
	@Getter @Setter String pointIncreaseSpeedUnit;
	@Getter @Setter String powerIncreaseSpeedUnit;
	@Getter @Setter BigDecimal pointCapacityEachBall;
	@Getter @Setter BigDecimal powerCapacityEachBall;
	@Getter @Setter String created;
	@Getter @Setter String updated;

}
