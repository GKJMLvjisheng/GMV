package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class EnergyTopicModel{
	private static final long serialVersionUID = 1L;
    @Getter @Setter private Integer topicId;
    @Getter @Setter private String question;
    @Getter @Setter private String choiceA;
    @Getter @Setter private String choiceB;
    @Getter @Setter private String choiceC;
    @Getter @Setter private String choiceRight;
    @Getter @Setter private String created;
}