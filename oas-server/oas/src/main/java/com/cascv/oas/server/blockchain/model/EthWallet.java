package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

public class EthWallet implements Serializable {

  private static final long serialVersionUID = 1L;
  @Getter @Setter private String uuid;
  @Getter @Setter private String userUuid;
  @Getter @Setter private String privateKey;
  @Getter @Setter private String publicKey;
  @Getter @Setter private String mnemonicList;
  @Getter @Setter private String mnemonicPath;
  @Getter @Setter private String address;
  @Getter @Setter private Integer crypto;
  @Getter @Setter private Integer backup;
  @Getter @Setter private String created;
  @Getter @Setter private String updated;
  @Getter @Setter private BigDecimal unconfirmedBalance;//待确认交易
}
