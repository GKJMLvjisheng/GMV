package com.cascv.oas.server.blockchain.vo;

import java.math.BigInteger;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class EthWalletTransfer implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Getter @Setter private String password;
  @Getter @Setter private String contract;
  @Getter @Setter private String toUserAddress;
  @Getter @Setter private BigInteger amount;
}