package com.cascv.oas.server.activity.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class ActivityRewardAdd implements Serializable {
	
    private static final long serialVersionUID = 1L;
    @Getter @Setter private String sourceUuid;
	@Getter @Setter private String rewardUuid;
	@Getter @Setter private BigDecimal baseValue;
	@Getter @Setter private BigDecimal increaseSpeed;
	@Getter @Setter private String increaseSpeedUnit;
	@Getter @Setter private BigDecimal maxValue;
	@Getter @Setter private Integer period;

}
