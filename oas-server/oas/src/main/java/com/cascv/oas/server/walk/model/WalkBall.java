package com.cascv.oas.server.walk.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class WalkBall {
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private BigDecimal stepNum;
	@Getter @Setter private Integer status;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;

}
