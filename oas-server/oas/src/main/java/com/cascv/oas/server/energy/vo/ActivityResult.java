package com.cascv.oas.server.energy.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ActivityResult implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private Integer sourceCode;
	@Getter @Setter private Integer status;

}
