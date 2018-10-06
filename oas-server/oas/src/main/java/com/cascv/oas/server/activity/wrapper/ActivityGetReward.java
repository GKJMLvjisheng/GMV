package com.cascv.oas.server.activity.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ActivityGetReward implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private Integer sourceCode;
	@Getter @Setter private Integer rewardCode;

}