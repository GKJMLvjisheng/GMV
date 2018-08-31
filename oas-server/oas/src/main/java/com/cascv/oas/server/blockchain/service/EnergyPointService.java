package com.cascv.oas.server.blockchain.service;


import java.math.BigDecimal;
import java.util.List;

import com.cascv.oas.server.energy.model.EnergyWallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.energy.mapper.EnergyWalletMapper;
import com.cascv.oas.server.blockchain.mapper.EnergyPointDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.EnergyPointDetail;
import com.cascv.oas.server.common.UuidPrefix;

@Service
public class EnergyPointService {
  
  @Autowired
  private EnergyWalletMapper energyWalletMapper;
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  @Autowired
  private PowerService powerService;

  @Autowired
  private EnergyPointDetailMapper energyPointDetailMapper;
  
  
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

  public List<EnergyPointDetail> searchEnergyPointDetail(
		  String userUuid, 
		  Integer activity, 
		  String dateFrom, 
		  String dateEnd){
    return energyPointDetailMapper.selectActivityByDate(userUuid, activity, dateFrom, dateEnd);
  }

  public Integer destroy(String userUuid){
    energyWalletMapper.deleteByUserUuid(userUuid);
    return 0;
  }

  //redeem
  public Integer redeem(String userUuid, Integer value) {
    EnergyWallet energyWallet = energyWalletMapper.selectByUserUuid(userUuid);
    UserWallet userWallet = userWalletMapper.selectByUserUuid(userUuid);
    if (energyWallet == null || userWallet == null || energyWallet.getPoint().compareTo(BigDecimal.valueOf(value)) < 0) {
      return 0;
    }
    energyWalletMapper.decreasePoint(energyWallet.getUuid(), BigDecimal.valueOf(value));
    userWalletMapper.increaseBalance(userWallet.getUuid(), powerService.exchange(energyWallet.getPower().intValue(), value));
    return value;
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
  
  public EnergyWallet findByUserUuid(String userUuid) {
	  return energyWalletMapper.selectByUserUuid(userUuid);
  }


}
