package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;

public interface DigitalCoinMapper {
  Integer insertSelective(DigitalCoin digitalCoin);
  DigitalCoin selectByContract(String contract);
  List<DigitalCoin> selectAll();
  Integer deleteByContract(String contract);
  List<ContractSymbol> selectContractSymbol(String userUuid);
}
