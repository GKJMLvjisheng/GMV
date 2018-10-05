package com.cascv.oas.server.blockchain.model;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EthConfigModel implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter private Integer id;
  @Getter @Setter private String activeNetwork;
  @Getter @Setter private String activeToken;
}

