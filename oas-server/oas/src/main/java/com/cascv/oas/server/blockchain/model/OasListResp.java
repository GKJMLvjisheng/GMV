package com.cascv.oas.server.blockchain.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class OasListResp {
	@Getter @Setter private List<String> uuids;
	@Getter @Setter private Integer status;
}
