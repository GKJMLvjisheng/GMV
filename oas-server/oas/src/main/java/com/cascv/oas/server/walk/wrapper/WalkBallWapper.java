package com.cascv.oas.server.walk.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

public class WalkBallWapper implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String uuid;
	@Getter @Setter private BigDecimal value;
	@Getter @Setter private Integer type;
	@Getter @Setter private String name;

}
