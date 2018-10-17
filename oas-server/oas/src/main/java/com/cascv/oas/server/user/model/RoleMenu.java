package com.cascv.oas.server.user.model;
/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class RoleMenu implements Serializable {
	private static final long serialVersionUID = 1L;
	@Setter @Getter private Integer roleMenuId;	
	@Setter @Getter private Integer roleId;
	@Setter @Getter private Integer menuId;
	@Setter @Getter private String created;
}
