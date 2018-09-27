package com.cascv.oas.server.blockchain.wrapper;

import java.math.BigDecimal;
import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class EthWalletTransfer implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String contract;
  @Getter @Setter private String toUserAddress;
  @Getter @Setter private BigInteger gasPrice;
  @Getter @Setter private BigInteger gasLimit;
  @Getter @Setter private BigDecimal amount;
  @Getter @Setter private String comment;
}