package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UUIDUtils;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.common.UuidPrefix;

@Service
public class UserWalletService {
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  public UserWallet create(String userUuid){
    UserWallet userWallet = new UserWallet();
    userWallet.setUuid(UUIDUtils.getPrefixUUID(UuidPrefix.USER_WALLET));
    userWallet.setUserUuid(userUuid);
    userWallet.setBalance(new BigDecimal(0));
    String now = DateUtils.dateTimeNow();
    userWallet.setCreated(now);
    userWallet.setUpdated(now);
    userWalletMapper.deleteByUserUuid(userUuid);
    userWalletMapper.insertSelective(userWallet);
    return userWallet;
  }

  public Integer destroy(String userUuid){
    userWalletMapper.deleteByUserUuid(userUuid);
    return 0;
  }

  public ErrorCode transfer(String fromUserUuid, String toUserUuid, BigDecimal value) {
    UserWallet fromUserWallet = userWalletMapper.selectByUserUuid(fromUserUuid);
    UserWallet toUserWallet = userWalletMapper.selectByUserUuid(toUserUuid);
    if (fromUserWallet == null || toUserWallet== null || fromUserWallet.getBalance().compareTo(value) < 0) {
      return ErrorCode.BALANCE_NOT_ENOUGH;
    }
    userWalletMapper.decreaseBalance(fromUserWallet.getUuid(), value);
    userWalletMapper.increaseBalance(toUserWallet.getUuid(), value);
    return ErrorCode.SUCCESS;
  }
}
