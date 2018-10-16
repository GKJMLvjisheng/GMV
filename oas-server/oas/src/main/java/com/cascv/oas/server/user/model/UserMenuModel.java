package com.cascv.oas.server.user.model;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class UserMenuModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Setter@Getter private Integer uuid;
	@Setter@Getter private Integer  menuId;
	@Setter@Getter private String  menuName;	
	@Getter@Setter private Integer menuLevel;
	@Getter@Setter private Integer menuParentId; 
}
