package com.cascv.oas.server.common;

import lombok.Getter;
import lombok.Setter;


public enum EthWalletDetailScope {

  COIN_TO_ETH(2, "提币","在线钱包转入交易钱包", 1),
  TRANSFER_OUT(3, "钱包转账-转出","交易钱包用户转账", 0),
  TRANSFER_IN(4, "钱包转账-转入","交易钱包用户转账", 1),
  ETH_TO_COIN(5, "充币","交易钱包转入在线钱包", 0);

  @Getter @Setter private Integer scope;
  @Getter @Setter private String title;
  @Getter @Setter private String subTitle;
  @Getter @Setter private Integer inOrOut;
  
  private EthWalletDetailScope(Integer scope, String title, String subTitle,Integer inOrOut) {
      this.scope = scope;
      this.title = title;
      this.subTitle = subTitle;
      this.inOrOut = inOrOut;
  }
}

