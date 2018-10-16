package com.cascv.oas.server.user.wrapper;
/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class RoleMenuViewModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Setter @Getter private Integer roleMenuId;	
	@Setter @Getter private Integer roleId;
	@Setter @Getter private Integer menuId;
	@Setter @Getter private Integer menuParentId;
	@Getter @Setter private String menuName;
	@Getter @Setter private Integer menuOrderId;
	@Setter @Getter private String created;
}
