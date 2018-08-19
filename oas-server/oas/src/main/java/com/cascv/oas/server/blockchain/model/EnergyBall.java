package com.cascv.oas.server.blockchain.model;

import lombok.Getter;
import lombok.Setter;

public class EnergyBall {
  @Getter @Setter private Integer id;
  @Getter @Setter private Integer type;
  @Getter @Setter private String name;
  @Getter @Setter private Integer value;
  @Getter @Setter private String startDate;
  @Getter @Setter private String endDate;
}
