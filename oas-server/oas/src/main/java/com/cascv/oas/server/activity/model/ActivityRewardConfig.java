package com.cascv.oas.server.activity.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class ActivityRewardConfig {
	@Getter @Setter private String uuid;
	@Getter @Setter private String sourceUuid;
	@Getter @Setter private String rewardUuid;
	@Getter @Setter private BigDecimal baseValue;
	@Getter @Setter private BigDecimal increaseSpeed;
	@Getter @Setter private String increaseSpeedUnit;
	@Getter @Setter private BigDecimal maxValue;
	@Getter @Setter private Integer period;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;

}
