package com.cascv.oas.core.model;

import com.cascv.oas.core.common.BaseEntity;

import lombok.Getter;
import lombok.Setter;

public class RolePerm extends BaseEntity {
	private static final long serialVersionUID = 1L;
	@Setter @Getter private Long roleId;
	@Setter @Getter private Long permId;
}
