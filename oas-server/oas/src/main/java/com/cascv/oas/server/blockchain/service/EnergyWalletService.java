package com.cascv.oas.server.blockchain.service;


import java.math.BigDecimal;
import com.cascv.oas.server.energy.model.EnergyWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.energy.mapper.EnergyWalletMapper;
import com.cascv.oas.server.common.UuidPrefix;

@Service
public class EnergyWalletService {
  
  @Autowired
  private EnergyWalletMapper energyWalletMapper;
  
  
  
  public EnergyWallet create(String userUuid){
    EnergyWallet energyWallet = new EnergyWallet();
    energyWallet.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
    energyWallet.setUserUuid(userUuid);
    energyWallet.setPoint(BigDecimal.ZERO);
    energyWallet.setPower(BigDecimal.ZERO);
    String now = DateUtils.dateTimeNow();
    energyWallet.setCreated(now);
    energyWallet.setUpdated(now);
    energyWalletMapper.deleteByUserUuid(userUuid);
    energyWalletMapper.insertSelective(energyWallet);
    return energyWallet;
  }


  public Integer destroy(String userUuid){
    energyWalletMapper.deleteByUserUuid(userUuid);
    return 0;
  }

  //deposit
  public Integer deposit(String category, String source, String activity, String userUuid, Integer value) {
    EnergyWallet energyWallet = energyWalletMapper.selectByUserUuid(userUuid);
    if (energyWallet == null) {
      return 0;
    }
    energyWalletMapper.increasePoint(energyWallet.getUuid(), BigDecimal.valueOf(value));
    return value;
  }
  
  


}
