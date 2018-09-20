package com.cascv.oas.server.energy.model.QAModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class EnergyAnswer {
    @Getter @Setter private Integer answerId;
    @Getter @Setter private Integer choiceRightId;
    @Getter @Setter private String created;
}