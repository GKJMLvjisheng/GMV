package com.cascv.oas.server.blockchain.mapper;

import com.cascv.oas.server.blockchain.model.EthWallet;

public interface EthWalletMapper {
  Integer insertSelective(EthWallet ethWallet);
  Integer update(EthWallet ethWallet);
  EthWallet selectByUuid(String uuid);
  EthWallet selectByUserUuid(String userUuid);
  Integer deleteByUuid(String uuid);
  Integer deleteByUserUuid(String userUuid);
  /**
   * 根据eth钱包地址获取当前用户的useruuid
   * @param address
   * @return
   */
  String getUserUuidByAddress(String address);
}
