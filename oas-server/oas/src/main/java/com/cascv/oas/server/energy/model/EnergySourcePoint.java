package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergySourcePoint {
    @Getter @Setter private String uuid;
    @Getter @Setter private Integer sourceCode;
    @Getter @Setter String sourceName;
    @Getter @Setter private String type;
    @Getter @Setter private BigDecimal baseValue;
    @Getter @Setter BigDecimal increaseSpeed;
    @Getter @Setter String increaseSpeedUnit;  
    @Getter @Setter BigDecimal maxValue;
    @Getter @Setter String period;
}