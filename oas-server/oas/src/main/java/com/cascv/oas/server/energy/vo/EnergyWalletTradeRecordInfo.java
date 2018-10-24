package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EnergyWalletTradeRecordInfo implements Serializable {
  private static final long serialVersionUID = 1L;
	@Getter @Setter private String name;
    @Getter @Setter private Integer inOrOut;
    @Getter @Setter private BigDecimal pointChange;
    @Getter @Setter private Integer status;
    @Getter @Setter private String created;
}
