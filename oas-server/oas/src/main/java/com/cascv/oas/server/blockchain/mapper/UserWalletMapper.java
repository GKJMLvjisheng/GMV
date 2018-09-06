package com.cascv.oas.server.blockchain.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.UserWallet;

public interface UserWalletMapper {
  Integer insertSelective(UserWallet userWallet);
  UserWallet selectByUuid(String uuid);
  UserWallet selectByUserUuid(String userUuid);
  Integer increaseBalance(@Param("uuid")  String uuid, @Param("value")  BigDecimal value);
  Integer decreaseBalance(@Param("uuid")  String uuid, @Param("value")  BigDecimal value);
  
  Integer deleteByUuid(String uuid);
  Integer deleteByUserUuid(String userUuid);
}
