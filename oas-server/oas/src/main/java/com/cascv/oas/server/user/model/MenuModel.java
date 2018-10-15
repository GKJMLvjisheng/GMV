package com.cascv.oas.server.user.model;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class MenuModel implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Setter @Getter private Long menuId;       	
	@Setter @Getter private String menuName;  
	@Setter@Getter private String menuOrderId;
	@Setter@Getter private Integer menuLevel;
	@Setter@Getter private Integer menuParentId;
	@Setter@Getter private String menuParentName;	
	@Setter @Getter private String desc;   	
	@Setter @Getter private Date created;   
	@Setter @Getter private Date updated;   
}
