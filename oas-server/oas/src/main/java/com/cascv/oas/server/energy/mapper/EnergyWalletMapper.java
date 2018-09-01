package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyWallet;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface EnergyWalletMapper {
  Integer insertSelective(EnergyWallet energyPoint);
  Integer insertSelectiveExceptPointPower(EnergyWallet energyPoint);

  EnergyWallet selectByUuid(String uuid);
  EnergyWallet selectByUserUuid(String userUuid);
  
  Integer increasePoint(@Param("uuid")  String uuid, @Param("value") BigDecimal value);
  Integer decreasePoint(@Param("uuid")  String uuid, @Param("value") BigDecimal value);
  
  Integer increasePower(@Param("uuid") String uuid, @Param("value") BigDecimal value);
  Integer decreasePower(@Param("uuid") String uuid, @Param("value") BigDecimal value);
  
  Integer deleteByUuid(String uuid);
  Integer deleteByUserUuid(String userUuid);
}
