package com.cascv.oas.server.common;

import lombok.Getter;
import lombok.Setter;


public enum EthWalletDetailScope {

  COIN_TO_ETH(2, "划账","在线钱包-划账", 1),
  TRANSFER_OUT(3, "钱包转账-转出","转给", 0),
  TRANSFER_IN(4, "钱包转账-转入","转自", 1),
  ETH_TO_COIN(5, "划账","交易钱包-划账", 0);

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

