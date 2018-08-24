package com.cascv.oas.server.blockchain.mapper;
import com.cascv.oas.server.blockchain.model.EthWallet;

public interface EthWalletMapper {
  Integer insertSelective(EthWallet ethHdWallet);
  EthWallet selectByUuid(String uuid);
  EthWallet selectByUserUuid(String userUuid);
  Integer deleteByUuid(String uuid);
  Integer deleteByUserUuid(String userUuid);
}
