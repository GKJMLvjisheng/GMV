package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class OasDetail implements Serializable{
	private static final long serialVersionUID = -3193470386514553995L;
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private BigDecimal value;
	@Getter @Setter private BigDecimal extra;
	@Getter @Setter private Integer status;
	@Getter @Setter private Integer type;
	@Getter @Setter private String txHash;
	@Getter @Setter private String remark;
	@Getter @Setter private String created;
	@Getter @Setter private String updated;
	
}