package com.cascv.oas.server.energy.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergyBallWrapper {
    @Getter @Setter private String uuid;
    @Getter @Setter private String type;
    @Getter @Setter private String name;
    @Getter @Setter private BigDecimal value;
    @Getter @Setter private String startDate;
    @Getter @Setter private String endDate;
}
