package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.common.UuidPrefix;

@Service
public class UserWalletService {
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  @Autowired 
  private UserWalletDetailMapper userWalletDetailMapper; 
  
  
  public UserWallet find(String userUuid){
    return userWalletMapper.selectByUserUuid(userUuid);
  }
  
  
  private void addDetail(UserWallet userWallet, UserWalletDetailScope userWalletDetailScope, BigDecimal value, String comment) {
	  UserWalletDetail userWalletDetail = new UserWalletDetail();
	  userWalletDetail.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.USER_WALLET_DETAIL));
	  userWalletDetail.setUserUuid(userWallet.getUserUuid());
	  userWalletDetail.setTitle(userWalletDetailScope.getTitle());
	  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle());
	  userWalletDetail.setInOrOut(userWalletDetailScope.getInOrOut());
	  userWalletDetail.setValue(value);
	  userWalletDetail.setCreated(DateUtils.getTime());
	  userWalletDetail.setComment(comment);
	  userWalletDetailMapper.insertSelective(userWalletDetail);
  }
  
  
  public UserWallet create(String userUuid){
    UserWallet userWallet = new UserWallet();
    userWallet.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.USER_WALLET));
    userWallet.setUserUuid(userUuid);
    userWallet.setBalance(BigDecimal.ZERO);
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
    if(value.compareTo(BigDecimal.ZERO) == 0) {
    	return ErrorCode.VALUE_CAN_NOT_BE_NULL;
    }
    else if (fromUserWallet == null || toUserWallet== null || fromUserWallet.getBalance().compareTo(value) < 0) {
      return ErrorCode.BALANCE_NOT_ENOUGH;
    }
    
    userWalletMapper.decreaseBalance(fromUserWallet.getUuid(), value);
    this.addDetail(fromUserWallet, UserWalletDetailScope.TRANSFER_OUT, value,"");
    
    userWalletMapper.increaseBalance(toUserWallet.getUuid(), value);
    this.addDetail(toUserWallet, UserWalletDetailScope.TRANSFER_IN, value,"");
    return ErrorCode.SUCCESS;
  }
}
