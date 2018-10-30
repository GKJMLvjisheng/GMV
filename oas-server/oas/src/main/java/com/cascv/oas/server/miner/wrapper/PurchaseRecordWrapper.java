package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class PurchaseRecordWrapper implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private String energyBallUuid;
	@Getter @Setter private String minerCode;
	@Getter @Setter private String minerName;
	@Getter @Setter private String minerDescription;
	@Getter @Setter private BigDecimal minerPrice;
	@Getter @Setter private Integer minerGrade;
	@Getter @Setter private BigDecimal minerPower;
	@Getter @Setter private Integer minerStatus;
	@Getter @Setter private String created;
	@Getter @Setter private String minerEndTime;
	@Getter @Setter private Integer minerNum;
	@Getter @Setter private BigDecimal priceSum;
	@Getter @Setter private Integer period;

}
