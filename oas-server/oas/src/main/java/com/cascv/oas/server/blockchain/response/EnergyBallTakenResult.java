package com.cascv.oas.server.blockchain.response;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EnergyBallTakenResult implements Serializable {
  private static final long serialVersionUID = 1L;
  @Setter @Getter private Integer newBonusPonit;
  @Setter @Getter private Integer newPower;
}
