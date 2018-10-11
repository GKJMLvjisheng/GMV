package com.cascv.oas.server.walk.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class WalkBallTokenRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String ballId;
	@Getter @Setter private BigDecimal stepNum;

}
