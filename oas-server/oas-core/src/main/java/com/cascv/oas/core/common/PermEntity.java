package com.cascv.oas.core.common;

import lombok.Getter;
import lombok.Setter;

public enum PermEntity {
	
  STEP_COUNT_PERMISSION(1,"计步"),
  EXCHANGE_POINT_FOR_TOKENS(2,"积分兑换代币"),
  USER_WALLET_WITHDRAW(3,"提币"),
  USER_WALLET_TRANSFER(4,"在线钱包-转账"),
  ETH_WALLET_REVERSE_WITHDRAW(5,"充币"),
  ETH_WALLET_TRANSFER(6,"交易钱包-转账"),
  MINER_PROMOTION_REWARD(7,"获得推广奖励");
  
  @Getter  private   final Integer code;
  @Getter  private   final String permName;
  
  private PermEntity(Integer code, String permName) {
      this.code = code;
      this.permName = permName;
  }
}
