package com.cascv.oas.server.energy.vo;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EnergyPowerChangeDetail {
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private Integer value;
	@Getter @Setter private BigDecimal powerChange;
	@Getter @Setter private String category;
	@Getter @Setter private String source;
	@Getter @Setter private String activity;
	@Getter @Setter private Integer inOrOut;
	@Getter @Setter private String comment;
	@Getter @Setter private String created;

}
