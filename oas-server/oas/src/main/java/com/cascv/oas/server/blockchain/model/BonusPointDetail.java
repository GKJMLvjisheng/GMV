package com.cascv.oas.server.blockchain.model;


import lombok.Getter;
import lombok.Setter;

public class BonusPointDetail {
  @Getter @Setter private Integer id;
  @Getter @Setter private Integer bonusPonitId;
  @Getter @Setter private Integer value;
  @Getter @Setter private String datetime;
  @Getter @Setter private String name;
}
