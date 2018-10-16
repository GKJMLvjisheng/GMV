package com.cascv.oas.server.user.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class AuthCode implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String mailCode;
	@Getter @Setter private String mobileCode;
	
}
