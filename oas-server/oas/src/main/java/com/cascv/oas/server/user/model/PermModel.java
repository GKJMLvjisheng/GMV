package com.cascv.oas.server.user.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

public class PermModel implements Serializable {
    
    
	private static final long serialVersionUID = 1L;
	public static int PTYPE_MENU = 1;     	//
    public static int PTYPE_BUTTON = 2; 	//
    
	@Setter @Getter private Long id;       	//
	@Setter @Getter private String name;   	//
	@Setter @Getter private Integer type;  	//
	@Setter @Getter private String value;   //
	@Setter @Getter private Date created;   //
	@Setter @Getter private Date updated;   //
}

