package com.cascv.oas.server.blockchain.mapper;

import java.util.List;

import com.cascv.oas.server.blockchain.model.UserCoin;

import org.apache.ibatis.annotations.Param;

public interface UserCoinMapper {
  Integer insertSelective(UserCoin userCoin);
  Integer deleteOne(@Param("userUuid") String userUuid, @Param("contract") String contract);
  UserCoin selectOne(@Param("userUuid") String userUuid, @Param("contract") String contract);
  List<UserCoin> selectAll(String userUuid);
}
