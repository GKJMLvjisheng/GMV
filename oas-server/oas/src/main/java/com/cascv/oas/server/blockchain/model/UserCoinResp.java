package com.cascv.oas.server.blockchain.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class UserCoinResp {

	@Setter @Getter private List<UserCoin> userCoin;
	@Setter @Getter private List<UserCoin> noShowCoin;
}
