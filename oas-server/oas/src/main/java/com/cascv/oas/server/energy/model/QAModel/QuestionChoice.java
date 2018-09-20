package com.cascv.oas.server.energy.model.QAModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class QuestionChoice {
    @Getter @Setter private Integer questionChoiceId;
    @Getter @Setter private Integer questionId;
    @Getter @Setter private Integer choiceId;
    @Getter @Setter private String created;
}