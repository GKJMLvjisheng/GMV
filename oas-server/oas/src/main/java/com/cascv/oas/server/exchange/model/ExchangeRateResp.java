package com.cascv.oas.server.exchange.model;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class ExchangeRateResp implements Serializable {

  private static final long serialVersionUID = 1L;
  @Setter @Getter private String time;
  @Setter @Getter private Integer currency;
  @Setter @Getter private BigDecimal rate;

}
