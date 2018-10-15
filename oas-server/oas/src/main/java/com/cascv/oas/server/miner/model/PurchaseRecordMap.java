package com.cascv.oas.server.miner.model;

import lombok.Getter;
import lombok.Setter;

public class PurchaseRecordMap {
	@Getter @Setter private String uuid;
	@Getter @Setter private String userUuid;
	@Getter @Setter private String minerCode;
	@Getter @Setter private String created;

}
