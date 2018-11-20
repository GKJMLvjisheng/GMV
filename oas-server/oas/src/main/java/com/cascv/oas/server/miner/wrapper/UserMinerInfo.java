package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserMinerInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String uuid;
	@Getter @Setter private String startTime;
	@Getter @Setter private String endTime;
	@Getter @Setter private Integer restriction;

}
