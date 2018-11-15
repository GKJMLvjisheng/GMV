package com.cascv.oas.server.blockchain.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class OasDetailResp{
	@Getter @Setter private String uuid;
	@Getter @Setter private String userName;
	@Getter @Setter private BigDecimal value;
	@Getter @Setter private BigDecimal extra;
	@Getter @Setter private Integer status;
	@Getter @Setter private String remark;
	@Getter @Setter private String address;
	@Getter @Setter private String created;
	
}