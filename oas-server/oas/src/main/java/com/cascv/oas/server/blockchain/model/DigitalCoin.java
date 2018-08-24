package com.cascv.oas.server.blockchain.model;

import lombok.Getter;
import lombok.Setter;

public class DigitalCoin {
  @Getter @Setter String contractAddress;
  @Getter @Setter String name;
  @Getter @Setter String symbol;
  @Getter @Setter String balance;
}