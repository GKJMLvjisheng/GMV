package com.gkyj.gmv.server.user.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;

import lombok.Getter;
import lombok.Setter;

public class TestData implements Serializable {
	private static final long serialVersionUID = 1L;
	
/*	@Getter @Setter private String minerName;
	@Getter @Setter private String minerDescription;
	@Getter @Setter private BigDecimal minerPrice;	
	@Getter @Setter private Integer circleNumber;	
	@Getter @Setter private String updated;
	@Getter @Setter private String loadPicturePath;*/
	
	@Getter @Setter private Integer box_id;
	@Getter @Setter private Integer circleNumber;
	@Getter @Setter private String  parameter;
	@Getter @Setter private String  picPath;
	@Getter @Setter private double value;
	@Getter @Setter private String time;
}
