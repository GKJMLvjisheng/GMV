package com.cascv.oas.server.blockchain.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.blockchain.mapper.EnergyPointMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.EnergyPoint;
import com.cascv.oas.server.blockchain.model.UserWallet;

@Service
public class EnergyPointService {
  
  @Autowired
  private EnergyPointMapper energyPointMapper;
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  @Autowired
  private PowerService powerService;
  
  //redeem
  public Integer redeem(Integer userId, Integer value) {
    EnergyPoint bonusPoint = energyPointMapper.selectByUserId(userId);
    UserWallet userWallet = userWalletMapper.selectByUserId(userId);
    if (bonusPoint == null || userWallet == null || bonusPoint.getBalance().compareTo(value) < 0) {
      return 0;
    }
    energyPointMapper.decreaseBalance(bonusPoint.getId(), value);
    userWalletMapper.increaseBalance(userWallet.getId(), powerService.exchange(bonusPoint.getPower(), value));
    return value;
  }
  
  //deposit
  public Integer deposit(String category, String source, String activity, Integer userId, Integer value) {
    EnergyPoint bonusPoint = energyPointMapper.selectByUserId(userId);
    if (bonusPoint == null) {
      return 0;
    }
    energyPointMapper.increaseBalance(bonusPoint.getId(), value);
    return value;
  }
  
}
