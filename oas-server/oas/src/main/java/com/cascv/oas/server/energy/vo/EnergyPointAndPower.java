package com.cascv.oas.server.energy.vo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class EnergyPointAndPower implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter @Setter private int point;
    @Getter @Setter private int power;
}
