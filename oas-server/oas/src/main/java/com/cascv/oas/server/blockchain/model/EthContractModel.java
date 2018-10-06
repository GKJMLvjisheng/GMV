package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EthContractModel implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String name;
  @Getter @Setter private String address;
  @Getter @Setter private String description;
}
