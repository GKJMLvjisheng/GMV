package com.cascv.oas.core.model;

import java.util.Date;

import com.cascv.oas.core.common.BaseEntity;

import lombok.Getter;
import lombok.Setter;

public class RoleModel extends BaseEntity{
	
	private static final long serialVersionUID = 1L;

	@Setter @Getter private Long id;       	// 角色id
	@Setter @Getter private String name;   	// 角色名，用于显示
	@Setter @Getter private String desc;   	// 角色描述
	@Setter @Getter private String value;   // 角色值，用于权限判断
	@Setter @Getter private Date created;   // 创建时间
	@Setter @Getter private Date updated;   // 修改时间
}
