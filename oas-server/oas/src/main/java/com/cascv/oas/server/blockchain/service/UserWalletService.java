package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.user.mapper.UserModelMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UserWalletService {
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  @Autowired
  private UserModelMapper userModelMapper;
  
  @Autowired 
  private UserWalletDetailMapper userWalletDetailMapper; 
  
  @Autowired
  private ExchangeRateService exchangeRateService;
  
  public UserWallet find(String userUuid){
    return userWalletMapper.selectByUserUuid(userUuid);
  }
  
  
  private void addDetail(UserWallet userWallet, String changeUserName, UserWalletDetailScope userWalletDetailScope, BigDecimal value, String comment, String remark) {
	  UserWalletDetail userWalletDetail = new UserWalletDetail();
	  userWalletDetail.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.USER_WALLET_DETAIL));
	  userWalletDetail.setUserUuid(userWallet.getUserUuid());
	  userWalletDetail.setTitle(userWalletDetailScope.getTitle());
	  /**
	   * 接口优化统一
	   */
	  Integer scope =userWalletDetailScope.getScope();
	  switch(scope){
	  case 1:
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle()+comment);
	      break;
	  case 2:
		  log.info("提币");		  
	      break;
	  case 3:
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle()+changeUserName);
	      break;
	  case 4:
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle()+changeUserName);
	      break;
	  case 5:
		  log.info("充币");
	      break;
	  default:
		  log.info("swicth-case-end");
	      break;
	  }
	  userWalletDetail.setInOrOut(userWalletDetailScope.getInOrOut());
	  userWalletDetail.setValue(value);
	  userWalletDetail.setCreated(DateUtils.getTime());
	  //userWalletDetail.setComment(comment);
	  userWalletDetail.setRemark(remark);
	  //userWalletDetail.setChangeUserName(changeUserName);
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

  public ErrorCode transfer(String fromUserUuid, String toUserUuid, BigDecimal value, String remark, String comment) {
    UserWallet fromUserWallet = userWalletMapper.selectByUserUuid(fromUserUuid);
    UserWallet toUserWallet = userWalletMapper.selectByUserUuid(toUserUuid);
    if(value.compareTo(BigDecimal.ZERO) == 0) {
    	return ErrorCode.VALUE_CAN_NOT_BE_NULL;
    }
    else if (fromUserWallet == null || toUserWallet== null || fromUserWallet.getBalance().compareTo(value) < 0) {
      return ErrorCode.BALANCE_NOT_ENOUGH;
    }
    
    String toUserName = userModelMapper.selectByUuid(toUserUuid).getName();
    String fromUserName = userModelMapper.selectByUuid(fromUserUuid).getName();
    
    userWalletMapper.decreaseBalance(fromUserWallet.getUuid(), value);
    this.addDetail(fromUserWallet, toUserName, UserWalletDetailScope.TRANSFER_OUT, value, comment, remark);
    
    userWalletMapper.increaseBalance(toUserWallet.getUuid(), value);
    this.addDetail(toUserWallet, fromUserName, UserWalletDetailScope.TRANSFER_IN, value,comment, remark);
    return ErrorCode.SUCCESS;
  }
  
  public void addFromEnergy(String userUuid, String time, BigDecimal point) {
	  UserWallet userWallet = userWalletMapper.selectByUserUuid(userUuid);
	  String changeUserName = userModelMapper.selectByUuid(userUuid).getName();
	  ReturnValue<BigDecimal> returnValue = exchangeRateService.exchangeFrom(
	        point, 
	        time, CurrencyCode.POINT);
	  BigDecimal token = returnValue.getData();
	  userWalletMapper.increaseBalance(userWallet.getUuid(), token);
	  /**
	   * 接口优化
	   */
	  //this.addDetail(userWallet, changeUserName, UserWalletDetailScope.ENERGY_TO_COIN, token, point.toString(), "");
	  this.addDetail(userWallet, changeUserName, UserWalletDetailScope.ENERGY_TO_COIN, token, point.toString(), "");
  } 
}
