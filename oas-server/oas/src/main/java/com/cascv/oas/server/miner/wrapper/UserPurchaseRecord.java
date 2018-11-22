package com.cascv.oas.server.miner.wrapper;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

public class UserPurchaseRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private String uuid;
	@Getter @Setter private String userName;
	@Getter @Setter private Integer restriction;
	@Getter @Setter private Integer amount;
	@Getter @Setter private Integer minerNum;
	@Getter @Setter private String minerName;
	@Getter @Setter private String startTime;
	@Getter @Setter private String endTime;
	@Getter @Setter private String created;

}
