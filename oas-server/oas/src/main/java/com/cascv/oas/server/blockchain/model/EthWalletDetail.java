package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EthWalletDetail implements Serializable {

  private static final long serialVersionUID = 1L;
  @Getter @Setter private String uuid;
  @Getter @Setter private String address;
  @Getter @Setter private BigDecimal value;
  @Getter @Setter private String title;
  @Getter @Setter private String subTitle;
  @Getter @Setter private Integer inOrOut;
  @Getter @Setter private String txHash;
  @Getter @Setter private Integer txResult;
  @Getter @Setter private String txNetwork;
  @Getter @Setter private String remark;
  @Getter @Setter private String created;
//  @Getter @Setter private String changeAddress;
}
