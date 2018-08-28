package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.util.List;

import com.cascv.oas.server.blockchain.config.TransferQuota;

import lombok.Getter;
import lombok.Setter;

public class EthWalletMultiTransfer implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String password;
  @Getter @Setter private String contract;
  @Getter @Setter private List<TransferQuota> quota;
}
