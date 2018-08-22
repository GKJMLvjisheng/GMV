package com.cascv.oas.server.blockchain.mapper;
import com.cascv.oas.server.blockchain.model.EthHdWallet;

public interface EthHdWalletMapper {
  Integer insertSelective(EthHdWallet ethHdWallet);
  EthHdWallet selectByUuid(String uuid);
  EthHdWallet selectByUserUuid(String userUuid);
  Integer deleteByUuid(String uuid);
  Integer deleteByUserUuid(String userUuid);
}
