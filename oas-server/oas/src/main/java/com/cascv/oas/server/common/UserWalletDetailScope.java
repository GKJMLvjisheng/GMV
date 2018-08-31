package com.cascv.oas.server.common;

import lombok.Getter;
import lombok.Setter;

public enum UserWalletDetailScope {
  ENERGY_REDEEM(1,"积分兑换OAS代币","积分消费"),
  COIN_REDEEM(2, "提币","在线钱包转入交易钱包"),
  TRANSFER_OUT(3, "钱包转账-转出","在线钱包用户转账"),
  TRANSFER_IN(4, "钱包转账-转入","在线钱包用户转账"),
  COIN_WITHDRAW(5, "充币","交易钱包转入在线钱包");

  @Getter @Setter private Integer scope;
  @Getter @Setter private String title;
  @Getter @Setter private String subTitle;

  private UserWalletDetailScope(Integer scope, String title, String subTitle) {
      this.scope = scope;
      this.title = title;
      this.subTitle = subTitle;
  }
}