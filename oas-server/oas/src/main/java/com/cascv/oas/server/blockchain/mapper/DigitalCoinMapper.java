package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import com.cascv.oas.server.blockchain.model.DigitalCoin;

public interface DigitalCoinMapper {
  Integer insertSelective(DigitalCoin digitalCoin);
  DigitalCoin selectByContract(String contract);
  List<DigitalCoin> selectAll();
  Integer deleteByContract(String contract);
}
