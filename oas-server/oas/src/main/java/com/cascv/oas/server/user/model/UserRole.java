package com.cascv.oas.server.user.model;
/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;
	@Setter @Getter private String uuid;
	@Setter @Getter private String name;
	@Setter @Getter private Integer roleId;
	@Setter @Getter private Integer userRoleId;
	@Setter @Getter private Integer rolePriority;
	@Setter @Getter private String roleName;
	@Setter @Getter private String created;
	
}
