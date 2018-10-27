package com.cascv.oas.server.user.model;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class UserFacility implements Serializable {
	private static final long serialVersionUID = 1L;
	@Setter @Getter private String uuid;
	@Setter @Getter private String IMEI;
	@Setter @Getter private Integer userIMEIId;
	@Setter @Getter private String created;
	
}
