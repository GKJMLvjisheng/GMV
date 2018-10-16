package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import com.cascv.oas.server.blockchain.model.EthContractModel;

public interface EthContractMapper {
  Integer insertSelective(EthContractModel ethContractModel);
  List<EthContractModel> selectAll();
  EthContractModel selectByName(String name);
  Integer deleteByName(String name);
}
