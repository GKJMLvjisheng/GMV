package com.cascv.oas.server.user.model;

import java.io.Serializable;


import lombok.Getter;
import lombok.Setter;

public class RolePerm implements Serializable {
	private static final long serialVersionUID = 1L;
	@Setter @Getter private Long roleId;
	@Setter @Getter private Long permId;
}
