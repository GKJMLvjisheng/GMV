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
    @Getter @Setter BigDecimal baseValue;
    @Getter @Setter BigDecimal increaseSpeed;
    @Getter @Setter String increaseSpeedUnit;
    @Getter @Setter BigDecimal maxValue;
}