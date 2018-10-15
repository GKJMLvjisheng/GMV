package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class UserMinerWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private String minerCode;
	@Getter @Setter private String minerName;
	@Getter @Setter private Integer minerGrade;
	@Getter @Setter private String minerDescription;
	@Getter @Setter private BigDecimal minerPrice;
	@Getter @Setter private BigDecimal minerEfficiency;
	@Getter @Setter private Integer minerPeriod;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;
	

}
