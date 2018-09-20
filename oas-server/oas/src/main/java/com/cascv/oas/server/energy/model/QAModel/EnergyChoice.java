package com.cascv.oas.server.energy.model.QAModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class EnergyChoice {
    @Getter @Setter private Integer choiceId;
    @Getter @Setter private String choiceContent;
    @Getter @Setter private String created;
}