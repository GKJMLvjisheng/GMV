package com.cascv.oas.server.energy.model.QAModel;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class UserQuestion {
    @Getter @Setter private Integer userQuestionId;
    @Getter @Setter private String userUuid;
    @Getter @Setter private Integer questionId;
    @Getter @Setter private String created;
}