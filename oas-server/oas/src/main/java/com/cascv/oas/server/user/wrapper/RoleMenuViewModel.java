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
	@Setter @Getter private Integer menuId;
	@Getter @Setter private String menuName;
	@Getter @Setter private String menuParentName;
	@Getter @Setter private Integer menuParentId;
	@Setter @Getter private String created;
}
