package com.cascv.oas.server.exchange.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.mapper.ExchangeRateMapper;
import com.cascv.oas.server.exchange.model.ExchangeRateModel;

@Service
public class ExchangeRateService {
   
  @Autowired
  private ExchangeRateMapper exchangeRateMapper;
  
  
  
  private Integer insertRate(String time, CurrencyCode currency, BigDecimal rate, String comment) {
    String now = DateUtils.getTime();
    ExchangeRateModel exchangeRateModel = new ExchangeRateModel();
    exchangeRateModel.setComment(comment);
    exchangeRateModel.setCreated(now);
    exchangeRateModel.setUpdated(now);
    exchangeRateModel.setCurrency(currency.getCode());
    exchangeRateModel.setTime(time);
    exchangeRateModel.setRate(rate);
    return exchangeRateMapper.insertSelective(exchangeRateModel);
  }

  public Integer setRate(String time, CurrencyCode currency, BigDecimal rate) {
    ExchangeRateModel exchangeRateModel = exchangeRateMapper.selectOne(time, currency.getCode());
    String now = DateUtils.getTime();
    if (exchangeRateModel == null) {
        return insertRate(time, currency, rate, "set");
    } else {
      exchangeRateModel.setUpdated(now);
      exchangeRateModel.setRate(rate);
      return exchangeRateMapper.updateSelective(exchangeRateModel);
    }
  }
  
  public ExchangeRateModel getRate(String time, CurrencyCode currency) {
    ExchangeRateModel exchangeRateModel = exchangeRateMapper.selectOne(time, currency.getCode());
    if (exchangeRateModel == null) {
      exchangeRateModel = exchangeRateMapper.selectNear(time, currency.getCode());
      if (exchangeRateModel != null) {
        insertRate(time, currency, exchangeRateModel.getRate(), "inherit");
      }
    } 
    return exchangeRateMapper.selectOne(time, currency.getCode());
  }
  
  
  public ReturnValue<BigDecimal> exchangeTo(BigDecimal value, String time, CurrencyCode currency) {
    ExchangeRateModel rate = getRate(time, currency);
    ReturnValue<BigDecimal> returnValue = new  ReturnValue<>();
    if (rate == null) {
      returnValue.setErrorCode(ErrorCode.NO_AVAILABLE_EXCHANGE_RATE);
      returnValue.setData(BigDecimal.ZERO);
    } else {
      returnValue.setData(value.multiply(rate.getRate()));
      returnValue.setErrorCode(ErrorCode.SUCCESS);
    }
    return returnValue;
  }
   
  public ReturnValue<BigDecimal> exchangeFrom(BigDecimal value, String time, CurrencyCode currency) {
    ReturnValue<BigDecimal> returnValue = new  ReturnValue<>();
    ExchangeRateModel rate = getRate(time, currency);
    if (rate == null || rate.getRate().compareTo(BigDecimal.valueOf(0.00000001)) < 0) {
      returnValue.setErrorCode(ErrorCode.NO_AVAILABLE_EXCHANGE_RATE);
      returnValue.setData(BigDecimal.ZERO);
    } else {
      returnValue.setErrorCode(ErrorCode.SUCCESS);
      returnValue.setData(value.divide(rate.getRate()));
    }
    return returnValue;
  }
}
