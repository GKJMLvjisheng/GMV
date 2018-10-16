package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EthWalletTransferResp implements Serializable{
  private static final long serialVersionUID = 1L;
  @Setter @Getter private String txHash;    
}