package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class TimeLimitInfo implements Serializable {
  private static final long serialVersionUID = 1L;
	@Getter @Setter private String startTime;
    @Getter @Setter private String endTime;
}
