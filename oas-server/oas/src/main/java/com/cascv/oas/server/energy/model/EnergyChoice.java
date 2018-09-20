package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class EnergyChoice {
    @Getter @Setter private Integer answerId;
    @Getter @Setter private Integer answerParentId;
    @Getter @Setter private Integer answerRight;
    @Getter @Setter private String answerContent;
    @Getter @Setter private String created;
}