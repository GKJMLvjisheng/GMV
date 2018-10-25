package com.cascv.oas.server.blockchain.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class SystemResq<T> {
	@Setter @Getter private List<T> list;
	@Setter @Getter private BigDecimal value;
}
