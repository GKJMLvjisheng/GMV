package com.cascv.oas.server.miner.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class MinerModel {
	
	@Getter @Setter private String minerCode;
	@Getter @Setter private String minerName;
	@Getter @Setter private String minerDescription;
	@Getter @Setter private BigDecimal minerPrice;
	@Getter @Setter private Integer minerGrade;
	@Getter @Setter private BigDecimal minerPower;
	@Getter @Setter private Integer minerPeriod;
	@Getter @Setter private Integer minerNum;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;
	

}
