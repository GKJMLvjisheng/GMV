package com.cascv.oas.server.energy.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ActivityResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String sourceUuid;
	@Getter @Setter private String sourceName;
	@Getter @Setter private Integer status;

}
