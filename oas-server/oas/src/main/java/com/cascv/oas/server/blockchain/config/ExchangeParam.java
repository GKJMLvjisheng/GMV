package com.cascv.oas.server.blockchain.config;

import lombok.Getter;
import lombok.Setter;

public class ExchangeParam{
  @Getter @Setter private Integer energyPointDaily;
  @Getter @Setter private Integer energyPowerDaily;
  @Getter @Setter private Double energyPointRate;
  @Getter @Setter private Double tokenRmbRate;
}
