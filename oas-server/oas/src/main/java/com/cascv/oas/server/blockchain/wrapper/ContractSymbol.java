package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class ContractSymbol implements Serializable{
	private static final long serialVersionUID = 1L;
	@Getter @Setter private String contract;
	@Getter @Setter private String symbol;
	@Getter @Setter private String name;
	@Getter @Setter private String userUuid;
   
}
