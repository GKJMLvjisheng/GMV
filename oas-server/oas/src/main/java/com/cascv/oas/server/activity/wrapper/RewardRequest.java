package com.cascv.oas.server.activity.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class RewardRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String rewardName;
	@Getter @Setter private String rewardDescription;

}