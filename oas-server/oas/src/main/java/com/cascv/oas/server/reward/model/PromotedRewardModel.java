/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
package com.cascv.oas.server.reward.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class PromotedRewardModel implements Serializable {
	private static final long serialVersionUID = 1L;
	@Getter @Setter private Integer promotedId;
	@Getter @Setter private String rewardName;
	@Getter @Setter private BigDecimal frozenRatio;
	@Getter @Setter private BigDecimal rewardRatio;
	@Getter @Setter private Integer maxPromotedGrade;
	@Getter @Setter private String created;
}