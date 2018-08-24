package com.cascv.oas.server.blockchain.service;

import java.math.BigInteger;

import org.springframework.stereotype.Service;

@Service
public class PowerService {
  public static final Integer powerFactor = 100;
  public BigInteger exchange(Integer power, Integer value) {
    BigInteger bigValue = BigInteger.valueOf(value);
    BigInteger bigPower = BigInteger.valueOf(power);
    BigInteger bigPowerFactor = BigInteger.valueOf(powerFactor);
    return bigValue.multiply(bigPower).divide(bigPowerFactor);
  } 
}
