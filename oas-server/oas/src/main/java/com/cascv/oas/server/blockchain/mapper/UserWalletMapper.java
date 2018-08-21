package com.cascv.oas.server.blockchain.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.UserWallet;

public interface UserWalletMapper {
  Integer insertSelective(UserWallet userWallet);
  UserWallet selectById(Integer id);
  UserWallet selectByUserId(Integer userId);
  
  Integer increaseBalance(@Param("id")  Integer id, @Param("value")  BigDecimal value);
  Integer decreaseBalance(@Param("id")  Integer id, @Param("value")  BigDecimal value);
  
  Integer deleteById(Integer id);
  Integer deleteByUserId(Integer userId);
}
