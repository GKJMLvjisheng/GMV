package com.gkyj.gmv.server.user.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class TestData implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String parameter;
	@Getter @Setter private String picPath;
	@Getter @Setter private String time;
	@Getter @Setter private String value;
}
