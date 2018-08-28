package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergySourcePoint {
    @Getter @Setter private Integer id;
    @Getter @Setter private Integer sourceCode;
    @Getter @Setter String sourceName;
    @Getter @Setter private String type;
    @Getter @Setter private BigDecimal pointSingle;
    @Getter @Setter BigDecimal pointTotal;
    @Getter @Setter BigDecimal pointIncreaseSpeed;
    @Getter @Setter String pointIncreaseSpeedUnitName;
    @Getter @Setter BigDecimal pointIncreaseSpeedUnitValue;
    @Getter @Setter BigDecimal pointMaxEachBall;
}