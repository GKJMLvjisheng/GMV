package com.cascv.oas.server.energy.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergyWalletBalance {
    @Getter @Setter private BigDecimal point;
    @Getter @Setter private BigDecimal power;
}
