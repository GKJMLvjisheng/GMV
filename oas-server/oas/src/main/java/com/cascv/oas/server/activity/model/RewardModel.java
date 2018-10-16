package com.cascv.oas.server.activity.model;

import lombok.Getter;
import lombok.Setter;

public class RewardModel {
	@Getter @Setter private Integer rewardCode;
	@Getter @Setter private String rewardName;
	@Getter @Setter private String rewardDescription;
	@Getter @Setter private String created;

}
