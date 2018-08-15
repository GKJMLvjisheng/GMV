package com.cascv.oas.server.user.model;

import java.util.Date;

import com.cascv.oas.core.common.BaseEntity;

import lombok.Getter;
import lombok.Setter;

public class PermModel extends BaseEntity {
    
    
	private static final long serialVersionUID = 1L;
	public static int PTYPE_MENU = 1;     	// 权限类型：菜�?
    public static int PTYPE_BUTTON = 2; 	// 权限类型：按�?
    
	@Setter @Getter private Long id;       	// 权限id
	@Setter @Getter private String name;   	// 权限名称
	@Setter @Getter private Integer type;  	// 权限类型�?1.菜单�?2.按钮
	@Setter @Getter private String value;   // 权限值，shiro的权限控制表达式
	@Setter @Getter private Date created;   // 创建时间
	@Setter @Getter private Date updated;   // 修改时间
}

