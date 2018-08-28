package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@ToString
public class UserEnergy {
    @Getter @Setter private Integer id;
    @Getter @Setter private Integer userId;
    @Getter @Setter private Integer energyBallId;
    @Getter @Setter private BigDecimal balancePoint;
    @Getter @Setter private BigDecimal balancePower;
    @Getter @Setter private Date timeCreated;
    @Getter @Setter private Date timeUpdated;
}