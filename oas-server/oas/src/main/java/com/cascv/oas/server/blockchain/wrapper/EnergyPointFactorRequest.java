package com.cascv.oas.server.blockchain.wrapper;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

public class EnergyPointFactorRequest implements Serializable{
  private static final long serialVersionUID = 1L;
  @Getter @Setter private String date;

}
