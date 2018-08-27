package com.cascv.oas.server.blockchain.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EnergyPointCategory implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter private Integer id;
  @Getter @Setter private String name;
  @Getter @Setter private Integer value;
  @Getter @Setter private Integer maxValue;
}
