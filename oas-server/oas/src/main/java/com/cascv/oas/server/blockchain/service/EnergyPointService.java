package com.cascv.oas.server.blockchain.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.mapper.EnergyPointMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.EnergyPoint;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.common.UuidPrefix;

@Service
public class EnergyPointService {
  
  @Autowired
  private EnergyPointMapper energyPointMapper;
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  @Autowired
  private PowerService powerService;
  
  public EnergyPoint create(String userUuid){
    EnergyPoint energyPoint = new EnergyPoint();
    energyPoint.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
    energyPoint.setUserUuid(userUuid);
    energyPoint.setBalance(0);
    energyPoint.setPower(0);
    String now = DateUtils.dateTimeNow();
    energyPoint.setCreated(now);
    energyPoint.setUpdated(now);
    energyPointMapper.deleteByUserUuid(userUuid);
    energyPointMapper.insertSelective(energyPoint);
    return energyPoint;
  }

  List<EnergyPointDetail> searchEnergyPointDetail(String userUuid, Integer activity, String dateFrom, String dateEnd){
    return energyPointMapper.selectActivityByDate(userUuid, activity, dateFrom, dateEnd);
  }

  public Integer destroy(String userUuid){
    energyPointMapper.deleteByUserUuid(userUuid);
    return 0;
  }

  //redeem
  public Integer redeem(String userUuid, Integer value) {
    EnergyPoint energyPoint = energyPointMapper.selectByUserUuid(userUuid);
    UserWallet userWallet = userWalletMapper.selectByUserUuid(userUuid);
    if (energyPoint == null || userWallet == null || energyPoint.getBalance().compareTo(value) < 0) {
      return 0;
    }
    energyPointMapper.decreaseBalance(energyPoint.getUuid(), value);
    userWalletMapper.increaseBalance(userWallet.getUuid(), powerService.exchange(energyPoint.getPower(), value));
    return value;
  }
  
  //deposit
  public Integer deposit(String category, String source, String activity, String userUuid, Integer value) {
    EnergyPoint energyPoint = energyPointMapper.selectByUserUuid(userUuid);
    if (energyPoint == null) {
      return 0;
    }
    energyPointMapper.increaseBalance(energyPoint.getUuid(), value);
    return value;
  }
  
  public EnergyPoint findByUserUuid(String userUuid) {
	  return energyPointMapper.selectByUserUuid(userUuid);
  }


}
