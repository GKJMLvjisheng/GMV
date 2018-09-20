package com.cascv.oas.server.energy.model;

import lombok.Getter;
import lombok.Setter;

public class ActivityCompletionStatus {
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private Integer source_code;
	@Getter @Setter private Integer status;

}
