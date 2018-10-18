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
  
  OAS(255, "OAS", ""),
  BTC(256, "BTC", ""),
  ETH(257, "ETH", ""),
  EOS(258, "EOS", ""),
  TRX(259, "TRX", ""),
  SNT(260, "SNT", ""),
  PMD(261, "PMD", ""),
  AAC(262, "AAC", ""),
  AE(263, "AE", ""),
  DSCB(264, "DSCB", ""),
  OMG(265, "OMG", ""),
  MIC(266, "MIC", ""),
  FX(267, "FX", ""),
  PRMI(268, "PRMI", ""),
  PWRC(269, "PWRC", ""),
  ZZEX(270, "ZZEX", ""),
  HRC(271, "HRC", ""),
  
  NOT_FIND(0,"NOTFIND","");
	
  
  @Setter @Getter private Integer code;
  @Setter @Getter private String name;
  @Setter @Getter private String symbol;
  
  private CurrencyCode(Integer code, String name, String symbol){
    this.code = code;
    this.name = name;
    this.symbol = symbol;
  }
  
  public static CurrencyCode get(Integer code) {
    for (CurrencyCode currencyCode : CurrencyCode.values()) {
      if (code == currencyCode.getCode())
        return currencyCode;
    }
    return null;
  }
  public static CurrencyCode getByName(String name) {
	  for(CurrencyCode currencyCode: CurrencyCode.values()) {
		  if(name.equals(currencyCode.getName())) {
			  return currencyCode;
		  }
	  }
	  return CurrencyCode.NOT_FIND;
  }
}
