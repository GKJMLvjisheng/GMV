package com.cascv.oas.server.blockchain.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.blockchain.mapper.BonusPointMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.BonusPoint;
import com.cascv.oas.server.blockchain.model.UserWallet;

@Service
public class BonusPointService {
  
  @Autowired
  private BonusPointMapper bonusPointMapper;
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  @Autowired
  private PowerService powerService;
  
  //redeem
  public Integer redeem(Integer userId, Integer value) {
    BonusPoint bonusPoint = bonusPointMapper.selectByUserId(userId);
    UserWallet userWallet = userWalletMapper.selectByUserId(userId);
    if (bonusPoint == null || userWallet == null || bonusPoint.getBalance().compareTo(value) < 0) {
      return 0;
    }
    bonusPointMapper.decreaseBalance(bonusPoint.getId(), value);
    userWalletMapper.increaseBalance(userWallet.getId(), powerService.exchange(bonusPoint.getPower(), value));
    return value;
  }
  
  //deposit
  public Integer deposit(Integer userId, Integer value) {
    BonusPoint bonusPoint = bonusPointMapper.selectByUserId(userId);
    if (bonusPoint == null) {
      return 0;
    }
    bonusPointMapper.increaseBalance(bonusPoint.getId(), value);
    return value;
  }
  
}
