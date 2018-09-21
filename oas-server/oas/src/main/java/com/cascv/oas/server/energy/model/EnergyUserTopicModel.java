package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@ToString
public class EnergyUserTopicModel{
	private static final long serialVersionUID = 1L;
    @Getter @Setter private String userTopicUuid;
    @Getter @Setter private String userUuid;
    @Getter @Setter private Integer topicId;
    @Getter @Setter private String created;
}