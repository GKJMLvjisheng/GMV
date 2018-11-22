package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class UserMinerInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private List<String> uuids;
	@Getter @Setter private String startTime;
	@Getter @Setter private String endTime;
	@Getter @Setter private Integer restriction;
}
