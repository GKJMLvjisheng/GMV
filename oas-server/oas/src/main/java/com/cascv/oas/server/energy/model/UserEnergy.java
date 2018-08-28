package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class UserEnergy {
    @Getter @Setter private String uuid;
    @Getter @Setter private String userUuid;
    @Getter @Setter private String energyBallUuid;
    @Getter @Setter private BigDecimal balancePoint;
    @Getter @Setter private BigDecimal balancePower;
    @Getter @Setter private String timeCreated;
    @Getter @Setter private String timeUpdated;
}