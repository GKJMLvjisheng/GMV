package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import com.cascv.oas.server.blockchain.model.EthNetworkModel;

public interface EthNetworkMapper {
  Integer insertSelective(EthNetworkModel ethNetworkModel);
  List<EthNetworkModel> selectAll();
  EthNetworkModel selectByName(String name);
  Integer deleteByName(String name);
}
