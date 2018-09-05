package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class EnergyBallTakenResult implements Serializable {
  private static final long serialVersionUID = 1L;
  @Setter @Getter private BigDecimal newEnergyPonit;
  @Setter @Getter private BigDecimal newPower;
}
