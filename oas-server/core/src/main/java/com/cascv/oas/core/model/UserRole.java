package com.cascv.oas.core.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;
	@Setter @Getter private Long userId;
	@Setter @Getter private Long roleId;
}
