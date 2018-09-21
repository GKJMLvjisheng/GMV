package com.cascv.oas.server.energy.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ActivityResultList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	  @Getter @Setter private List<ActivityResult> activityResultList;

}
