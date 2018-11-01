package com.cascv.oas.server.activity.model;

import lombok.Getter;
import lombok.Setter;

public class ActivityModel {
	
	@Getter @Setter private String sourceUuid;
	@Getter @Setter private Integer sourceCode;
	@Getter @Setter private String sourceName;
	@Getter @Setter private String type;
	@Getter @Setter String created;

}
