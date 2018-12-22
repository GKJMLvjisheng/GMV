package com.gkyj.gmv.server.activiti.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class FirstProject {
	//申请流程
	@Getter @Setter String keyName;
	@Getter @Setter String processId;
 
	//申请人
	@Getter @Setter private String applyUser;
	@Getter @Setter private String reason;
	@Getter @Setter private Date applyTime;
    @Getter @Setter private String applyStatus;

    // 审核人
    @Getter @Setter private String auditor;
    @Getter @Setter private String result;
    @Getter @Setter private Date auditTime;
    
    @Getter @Setter private String toWho;
}
