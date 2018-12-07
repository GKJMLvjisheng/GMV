package com.gkyj.gmv.server.load.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class LoadModel {
	
	@Getter @Setter private String minerName;
	@Getter @Setter private String minerDescription;
	@Getter @Setter private BigDecimal minerPrice;	
	@Getter @Setter private Integer circleNumber;	
	@Getter @Setter private String updated;
	@Getter @Setter private String loadPicturePath;
	

}
