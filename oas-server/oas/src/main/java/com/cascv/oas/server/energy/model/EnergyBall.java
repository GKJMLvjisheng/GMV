package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergyBall {
    @Getter
    @Setter
    private Integer id;
    @Getter
    @Setter
    private Integer userId;
    @Getter
    @Setter
    private Integer pointSource;
    @Getter
    @Setter
    private BigDecimal point;
    @Getter
    @Setter
    private Integer powerSource;
    @Getter
    @Setter
    private BigDecimal power;
    @Getter
    @Setter
    private Integer status;
}