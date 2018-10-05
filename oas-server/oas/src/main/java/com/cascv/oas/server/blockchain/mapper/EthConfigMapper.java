package com.cascv.oas.server.blockchain.mapper;

import com.cascv.oas.server.blockchain.model.EthConfigModel;

public interface EthConfigMapper {
  EthConfigModel selectOne();
  Integer insertSelective(EthConfigModel ethConfigModel);
  Integer update(EthConfigModel ethConfigModel);
  Integer deleteById(Integer id);
}
