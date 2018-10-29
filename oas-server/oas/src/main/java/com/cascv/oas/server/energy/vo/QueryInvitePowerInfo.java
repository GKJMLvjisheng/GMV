package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class QueryInvitePowerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private Integer SumUserInvited;//邀请总人数
	@Getter @Setter private BigDecimal SumPowerPromoted;//累计算力提升
	
}
