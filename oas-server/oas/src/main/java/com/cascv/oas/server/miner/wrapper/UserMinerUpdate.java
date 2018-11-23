package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class UserMinerUpdate implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private List<UserMinerInfo> umInfos;

}
