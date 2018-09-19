package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class BackupsWalletResult implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Setter @Getter private BigDecimal newPower;

}
