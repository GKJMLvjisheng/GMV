package com.cascv.oas.server.blockchain.mapper;
import com.cascv.oas.server.blockchain.model.EthHdWallet;

public interface EthHdWalletMapper {
  Integer insertSelective(EthHdWallet ethHdWallet);
  EthHdWallet selectById(String id);
  EthHdWallet selectByUserId(String userId);
  Integer deleteById(Integer id);
  Integer deleteByUserId(Integer userId);
}
