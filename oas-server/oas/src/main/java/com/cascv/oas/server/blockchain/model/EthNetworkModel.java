package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EthNetworkModel implements Serializable {
  private static final long serialVersionUID = 1L;
  @Setter @Getter private String name;
  @Setter @Getter private String provider;
  @Setter @Getter private String description;
}
