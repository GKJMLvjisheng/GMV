package com.cascv.oas.server.energy.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EnergyBallTokenRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	@Setter @Getter private String ballId;
}
