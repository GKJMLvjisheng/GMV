package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyWallet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface EnergyWalletMapper {
  Integer insertSelective(EnergyWallet energyPoint);

  EnergyWallet selectByUuid(String uuid);
  EnergyWallet selectByUserUuid(String userUuid);
  
  Integer increaseBalance(@Param("uuid")  String uuid, @Param("value")  Integer value);
  Integer decreaseBalance(@Param("uuid")  String uuid, @Param("value")  Integer value);
  
  Integer increasePower(@Param("uuid") String uuid, @Param("value") Integer value);
  Integer decreasePower(@Param("uuid") String uuid, @Param("value") Integer value);
  
  Integer deleteByUuid(String uuid);
  Integer deleteByUserUuid(String userUuid);
}
