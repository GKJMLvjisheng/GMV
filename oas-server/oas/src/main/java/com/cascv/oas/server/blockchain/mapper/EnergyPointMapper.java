package com.cascv.oas.server.blockchain.mapper;

import org.apache.ibatis.annotations.Param;

import com.cascv.oas.server.blockchain.model.EnergyPoint;

public interface EnergyPointMapper {
  Integer insertSelective(EnergyPoint energyPoint);
  
  EnergyPoint selectByUuid(String uuid);
  EnergyPoint selectByUserUuid(String userUuid);
  
  Integer increaseBalance(@Param("uuid")  String uuid, @Param("value")  Integer value);
  Integer decreaseBalance(@Param("uuid")  String uuid, @Param("value")  Integer value);
  
  Integer increasePower(@Param("uuid") String uuid, @Param("value") Integer value);
  Integer decreasePower(@Param("uuid") String uuid, @Param("value") Integer value);
  
  Integer deleteByUuid(String uuid);
  Integer deleteByUserUuid(String userUuid);
}
