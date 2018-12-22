package com.gkyj.gmv.server.activiti.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class FirstProjectTask {
	@Getter @Setter private String id;
	@Getter @Setter private String name;
	@Getter @Setter private FirstProject project;
	@Getter @Setter private Date createTime;
}
