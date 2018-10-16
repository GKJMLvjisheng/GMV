package com.cascv.oas.server.user.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class RoleModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Setter @Getter private Long roleId;       	// 角色id
	@Setter @Getter private String roleName;   	// 角色名，用于显示
	@Setter @Getter private String desc;   	// 角色描述
	@Setter @Getter private String value;   // 角色值，用于权限判断
	@Setter @Getter private Date created;   // 创建时间
	@Setter @Getter private Date updated;   // 修改时间
}
