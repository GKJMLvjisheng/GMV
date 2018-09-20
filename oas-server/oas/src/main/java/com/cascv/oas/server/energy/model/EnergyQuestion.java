package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
@ToString
public class EnergyQuestion {
    @Getter @Setter private Integer questionId;
    @Getter @Setter private Integer questionDone;
    @Getter @Setter private String questionContent;
    @Getter @Setter private String created;
}