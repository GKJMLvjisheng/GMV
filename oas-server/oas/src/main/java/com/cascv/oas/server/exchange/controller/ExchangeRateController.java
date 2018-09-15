package com.cascv.oas.server.exchange.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.model.ExchangeRateModel;
import com.cascv.oas.server.exchange.model.ExchangeRateReq;
import com.cascv.oas.server.exchange.model.ExchangeRateResp;
import com.cascv.oas.server.exchange.service.ExchangeRateService;

@RestController
@RequestMapping(value = "/api/v1/exchange")
public class ExchangeRateController {

  
  @Autowired
  private ExchangeRateService exchangeRateService;
  
  @PostMapping(value = "/inquire")
  @ResponseBody
  public ResponseEntity<?> inquire(@RequestBody ExchangeRateReq req) {
      ExchangeRateResp resp = new ExchangeRateResp();
      resp.setCurrency(req.getCurrency());
      resp.setTime(req.getTime());
      
      String time = req.getTime();
      Integer currency = req.getCurrency();
      if (time == null)
        return new ResponseEntity.Builder<ExchangeRateResp>()
            .setData(resp)
            .setErrorCode(ErrorCode.NO_TIME_SPECIFIED).build();
      CurrencyCode currencyCode;
      if (currency == null) {
        currencyCode = null;
      } else {
        currencyCode = CurrencyCode.get(currency);
      }
      if (currencyCode == null)
        return new ResponseEntity.Builder<ExchangeRateResp>()
            .setData(resp)
            .setErrorCode(ErrorCode.NO_CURRENCY_SPECIFIED).build();
        
      ExchangeRateModel exchangeRateModel = exchangeRateService.getRate(time, currencyCode);
      if (exchangeRateModel == null)
        return new ResponseEntity.Builder<ExchangeRateResp>()
            .setData(resp)
            .setErrorCode(ErrorCode.NO_AVAILABLE_EXCHANGE_RATE).build();
      resp.setRate(exchangeRateModel.getRate());
      return new ResponseEntity.Builder<ExchangeRateResp>()
          .setData(resp)
          .setErrorCode(ErrorCode.SUCCESS).build();
  }
}
