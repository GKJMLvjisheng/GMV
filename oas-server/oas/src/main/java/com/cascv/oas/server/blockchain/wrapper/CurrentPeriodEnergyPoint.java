package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class CurrentPeriodEnergyPoint implements Serializable {
  private static final long serialVersionUID = 1L;
	@Setter @Getter private Integer producedEnergyPoint;
  @Setter @Getter private Integer consumedEnergyPoint;
}