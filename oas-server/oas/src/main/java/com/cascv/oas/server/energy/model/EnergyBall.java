package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class EnergyBall {
    @Getter @Setter private String uuid;
    @Getter @Setter private String userUuid;
    @Getter @Setter private Integer pointSource;
    @Getter @Setter private BigDecimal point;
    @Getter @Setter private Integer powerSource;
    @Getter @Setter private BigDecimal power;
    @Getter @Setter private Integer status;
    @Getter @Setter private String timeCreated;
    @Getter @Setter private String timeUpdated;
}