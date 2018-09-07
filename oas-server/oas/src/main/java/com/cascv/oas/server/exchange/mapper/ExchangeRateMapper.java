package com.cascv.oas.server.exchange.mapper;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.exchange.model.ExchangeRateModel;

public interface ExchangeRateMapper {
  Integer insertSelective(ExchangeRateModel exchangeRate);
  Integer updateSelective(ExchangeRateModel exchangeRate);
  ExchangeRateModel selectOne(@Param("time") String time, @Param("currency") Integer currency);
  ExchangeRateModel selectNear(@Param("time") String time, @Param("currency") Integer currency);
  
}
