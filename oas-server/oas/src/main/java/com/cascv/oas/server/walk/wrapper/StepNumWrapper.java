package com.cascv.oas.server.walk.wrapper;

import java.io.Serializable;
import java.util.List;


import lombok.Getter;
import lombok.Setter;

public class StepNumWrapper implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Getter @Setter private List<StepNumQuota> quota;

}
