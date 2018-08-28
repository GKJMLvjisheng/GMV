package com.cascv.oas.server.energy.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
@ToString
public class EnergyCheckinResult implements Serializable {
  private static final long serialVersionUID = 1L;
  @Setter @Getter private BigDecimal newEnergyPoint;
  @Setter @Getter private BigDecimal newPower;
}
