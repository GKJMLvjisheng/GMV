package com.cascv.oas.server.exchange.constant;

import lombok.Getter;
import lombok.Setter;

public enum CurrencyCode {
  POINT(1, "POINT", "_"),
  CNY(2, "CNY", ""),
  USD(3, "USD", ""),
  JPY(4, "JPY", ""),
  EUR(5, "EUR", ""),
  DEM(6, "DEM", ""),
  FRF(7, "FRF", ""),
  CAD(8, "CAD", ""),
  AUD(9, "AUD", ""),
  HKD(10, "HKD", ""),
  OAS(255, "OAS", "");
  
  @Setter @Getter private Integer code;
  @Setter @Getter private String name;
  @Setter @Getter private String symbol;
  
  private CurrencyCode(Integer code, String name, String symbol){
    this.code = code;
    this.name = name;
    this.symbol = symbol;
  }
  
  public static CurrencyCode get(Integer code) {
    CurrencyCode [] codeSet = {
        CurrencyCode.POINT,
        CurrencyCode.CNY,
        CurrencyCode.USD,
        CurrencyCode.JPY,
        CurrencyCode.EUR,
        CurrencyCode.DEM,
        CurrencyCode.FRF,
        CurrencyCode.CAD,
        CurrencyCode.HKD,
        CurrencyCode.OAS
    };
    for (CurrencyCode currencyCode:codeSet) {
      if (code == currencyCode.getCode())
        return currencyCode;
    }
    return null;
  }
}
