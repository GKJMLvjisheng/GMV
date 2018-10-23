package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class IMEIModel implements Serializable {
	
	private static final long serialVersionUID = 1L;	
	@Getter @Setter private String name;
	@Getter @Setter private String IMEI;
	@Getter @Setter private Integer roleId;	
}
