package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class PowerService {
  public static final Integer powerFactor = 100;
  public BigDecimal exchange(Integer power, Integer value) {
    BigDecimal bigValue = BigDecimal.valueOf(value);
    BigDecimal bigPower = BigDecimal.valueOf(power);
    BigDecimal bigPowerFactor = BigDecimal.valueOf(powerFactor);
    return bigValue.multiply(bigPower).divide(bigPowerFactor);
  } 
}
