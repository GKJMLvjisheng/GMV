package com.cascv.oas.server.energy.model.QAModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class QuestionAnswer {
    @Getter @Setter private Integer questionAnswerId;
    @Getter @Setter private Integer questionId;
    @Getter @Setter private Integer answerId;
    @Getter @Setter private String created;
}