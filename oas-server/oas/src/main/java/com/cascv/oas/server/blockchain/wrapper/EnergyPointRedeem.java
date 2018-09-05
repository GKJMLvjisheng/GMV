package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

public class EnergyPointRedeem implements Serializable {

  private static final long serialVersionUID = 1L;
  @Setter @Getter private String date;
  @Setter @Getter private Integer type;
  @Setter @Getter private BigDecimal rate;
  @Setter @Getter private BigDecimal amount;
    
}