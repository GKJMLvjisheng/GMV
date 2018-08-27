package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class EnergySourcePoint {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private Integer sourceCode;
    @Getter
    @Setter
    private String sourceName;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private Integer pointSingle;
    @Getter
    @Setter
    private Integer pointTotal;
    @Getter
    @Setter
    private BigDecimal pointIncreaseSpeed;
    @Getter
    @Setter
    private Integer pointEachBall;
}