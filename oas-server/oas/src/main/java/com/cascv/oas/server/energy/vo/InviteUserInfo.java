package com.cascv.oas.server.energy.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class InviteUserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String uuid;
	@Getter @Setter private Integer inviteFrom;
	@Getter @Setter private Integer inviteCode;
	
}
