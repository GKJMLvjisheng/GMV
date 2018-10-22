package com.cascv.oas.server.common;

import lombok.Getter;
import lombok.Setter;

public enum UserWalletDetailScope {
  ENERGY_TO_COIN(1,"积分兑换OAS代币","积分消费", 1),
  COIN_TO_ETH(2, "提币","在线钱包转入交易钱包", 0),
  TRANSFER_OUT(3, "钱包转账-转出","转给", 0),
  TRANSFER_IN(4, "钱包转账-转入","转自", 1),
  ETH_TO_COIN(5, "充币","交易钱包转入在线钱包", 1),
  PURCHASE_MINER(6, "购买矿机", "购买", 0),
  MINER_ADD_COIN(7,"矿机推广奖励","购买矿机",1),
  FROZEN_ADD_COIN(8,"矿机推广奖励","购买矿机",1);

  @Getter @Setter private Integer scope;
  @Getter @Setter private String title;
  @Getter @Setter private String subTitle;
  @Getter @Setter private Integer inOrOut;
  
  private UserWalletDetailScope(Integer scope, String title, String subTitle, Integer inOrOut) {
      this.scope = scope;
      this.title = title;
      this.subTitle = subTitle;
      this.inOrOut = inOrOut;
  }
}