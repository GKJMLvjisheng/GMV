package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergySourcePoint {
    @Getter @Setter private Integer id;
    @Getter @Setter private Integer sourceCode;
    @Getter @Setter private String type;
    @Getter @Setter private Integer pointSingle;
    @Getter @Setter Integer pointTotal;
    @Getter @Setter BigDecimal pointIncreaseSpeed;
    @Getter @Setter String pointIncreaseSpeedUnitName;
    @Getter @Setter Integer pointIncreaseSpeedUnitValue;
    @Getter @Setter Integer pointMaxEachBall;
}