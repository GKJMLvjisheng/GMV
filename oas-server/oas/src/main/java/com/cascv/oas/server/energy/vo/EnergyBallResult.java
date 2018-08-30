package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.cascv.oas.server.energy.model.EnergyBall;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class EnergyBallResult  implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private List<EnergyBall> energyBallList;
  @Getter @Setter private BigDecimal ongoingEnergySummary;
}
