package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class MobileModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String mobile;
	@Getter @Setter private String content;
	
}
