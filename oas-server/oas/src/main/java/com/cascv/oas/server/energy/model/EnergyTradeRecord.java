package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
public class EnergyTradeRecord {
    @Getter @Setter private String uuid;
    @Getter @Setter private String userUuid;
    @Getter @Setter private String energyBallUuid;
    @Getter @Setter private Integer inOrOut;
    @Getter @Setter private BigDecimal pointChange;
    @Getter @Setter private BigDecimal powerChange;
    @Getter @Setter private Integer status;
    @Getter @Setter private String created;
    @Getter @Setter private String updated;
    @Getter @Setter private BigDecimal restPoint;
}