package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;

@Service
public class UserWalletService {
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  public ErrorCode transfer(Integer fromUserId, Integer toUserId, BigDecimal value) {
    UserWallet fromUserWallet = userWalletMapper.selectByUserId(fromUserId);
    UserWallet toUserWallet = userWalletMapper.selectByUserId(toUserId);
    if (fromUserWallet == null || toUserWallet== null || fromUserWallet.getBalance().compareTo(value) < 0) {
      return ErrorCode.BALANCE_NOT_ENOUGH;
    }
    userWalletMapper.decreaseBalance(fromUserWallet.getId(), value);
    userWalletMapper.increaseBalance(toUserWallet.getId(), value);
    return ErrorCode.SUCCESS;
  }
}
