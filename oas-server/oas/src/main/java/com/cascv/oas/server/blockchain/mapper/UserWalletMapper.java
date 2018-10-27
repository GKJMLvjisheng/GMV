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
  
  //提币操作更新用户拥有的代币和待确认的交易
  Integer changeBalanceAndUnconfimed(@Param("userUUid")String userId,@Param("value")BigDecimal value,@Param("unValue")BigDecimal unValue,@Param("updated")String updated);
  /**
   * 获取system的钱包
   * @return
   */
  UserWallet getSystemWallet();
  /**
   * 获取firstone钱包
   * @return
   */
  UserWallet getFirstOneWallet();
}
