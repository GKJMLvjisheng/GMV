package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class AccountTypeMiner implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String minerName;
	@Getter @Setter private Integer minerNum;

}
