package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class InquireRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String minerCode;
	@Getter @Setter private String minerName;

}
