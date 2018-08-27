package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class UserEnergy {
    @Getter @Setter private Integer userId;
    @Getter @Setter private Integer currentPoint;
    @Getter @Setter private Integer currentPower;
}