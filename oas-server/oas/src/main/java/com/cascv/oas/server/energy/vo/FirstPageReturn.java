package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class FirstPageReturn implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private BigDecimal power;
	@Getter @Setter private BigDecimal point;

	public void energyBallReturn(EnergyBallResult energyBallResult){
		this.energyBallReturn(energyBallResult);;
	}

}
