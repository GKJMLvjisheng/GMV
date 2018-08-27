package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergySourcePower {
    @Getter @Setter Integer id;
    @Getter @Setter Integer sourceCode;
    @Getter @Setter String sourceName;
    @Getter @Setter String type;
    @Getter @Setter Integer powerSingle;
    @Getter @Setter Integer powerTotal;
    @Getter @Setter BigDecimal powerIncreaseSpeed;
    @Getter @Setter String powerIncreaseSpeedUnitName;
    @Getter @Setter Integer powerIncreaseSpeedUnitValue;
    @Getter @Setter Integer powerMaxEachBall;
}