package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergySourcePower {
    @Getter @Setter String uuid;
    @Getter @Setter Integer sourceCode;
    @Getter @Setter String sourceName;
    @Getter @Setter String type;
    @Getter @Setter BigDecimal powerSingle;
    @Getter @Setter BigDecimal powerTotal;
    @Getter @Setter BigDecimal powerIncreaseSpeed;
    @Getter @Setter String powerIncreaseSpeedUnitName;
    @Getter @Setter BigDecimal powerIncreaseSpeedUnitValue;
    @Getter @Setter BigDecimal powerMaxEachBall;
}