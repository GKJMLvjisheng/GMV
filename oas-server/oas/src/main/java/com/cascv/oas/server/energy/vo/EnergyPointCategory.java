package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EnergyPointCategory implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter private Integer id;
  @Getter @Setter private String name;
  @Getter @Setter private BigDecimal value;
  @Getter @Setter private BigDecimal maxValue;
}
