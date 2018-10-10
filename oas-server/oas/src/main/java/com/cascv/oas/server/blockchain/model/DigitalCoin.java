package com.cascv.oas.server.blockchain.model;


import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class DigitalCoin implements Serializable {
  private static final long serialVersionUID = 1L;
  @Getter @Setter String contract;
  @Getter @Setter String name;
  @Getter @Setter String symbol;
  @Getter @Setter String network;
  @Getter @Setter BigDecimal weiFactor;
  @Getter @Setter BigDecimal supply;
}