package com.cascv.oas.server.user.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class MenuModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Setter @Getter private Integer menuId;       	
	@Setter @Getter private String menuName;  
	@Setter@Getter private Integer menuOrderId;
	@Setter@Getter private Integer menuLevel;
	@Setter@Getter private Integer menuParentId;
	@Setter@Getter private String menuParentName;	
	@Setter @Getter private String desc;   	
	@Setter @Getter private String created;   
	@Setter @Getter private String updated;   
}
