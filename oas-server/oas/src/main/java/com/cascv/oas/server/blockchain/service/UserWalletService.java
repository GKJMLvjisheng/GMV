package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.constant.OasEventEnum;
import com.cascv.oas.server.blockchain.mapper.OasDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.OasDetail;
import com.cascv.oas.server.blockchain.model.OasDetailResp;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.user.mapper.UserModelMapper;
import com.cascv.oas.server.user.model.UserModel;

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
  
  @Autowired
  private OasDetailMapper oasDetailMapper;
  
  @Autowired
  private EthWalletService ethWalletService;
  
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
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle());		  
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
//	  userWalletDetail.setComment(comment);
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

  public ErrorCode withdraw(OasDetail oasDetail){
	  oasDetail.setExtra(new BigDecimal(oasDetailMapper.getOasExtra()));
	//用户的在线钱包代币变化
	  UserWallet userWallet = userWalletMapper.selectByUserUuid(oasDetail.getUserUuid());
	  BigDecimal value = new BigDecimal(0);
	  if(userWallet!=null) {
		  if(userWallet.getBalance().compareTo(oasDetail.getValue()) == -1) {
			  return ErrorCode.BALANCE_NOT_ENOUGH;
		  }
		  value = userWallet.getBalance().subtract(oasDetail.getValue());
		  if(value.compareTo(oasDetail.getExtra()) == -1) {
			  return ErrorCode.OAS_EXTRA_MONEY_NOT_ENOUGH;
		  }
		  value = value.subtract(oasDetail.getExtra()); //减去手续费
	  }
	  oasDetail.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.OAS_DETAIL));
	  String now = DateUtils.dateTimeNow();
	  oasDetail.setCreated(now);
	  oasDetail.setUpdated(now);
	  oasDetail.setStatus(OasEventEnum.FORSURE.getCode());
	  oasDetail.setType(OasEventEnum.OAS_OUT.getCode());
	  //插入充币提币表
	  oasDetailMapper.insertSelective(oasDetail);
	  
	  //查询system账号，给system转手续费
	  UserModel systemInfo = oasDetailMapper.getSystemUserInfo();
	  if(systemInfo!=null) {
		  UserWallet systemWallet = userWalletMapper.selectByUserUuid(systemInfo.getUuid());
		  Integer sResult = userWalletMapper.increaseBalance(systemWallet.getUuid(), oasDetail.getExtra());
		  if(sResult == 0) {
			  return ErrorCode.UPDATE_FAILED;
		  }
	  }
	  return userWalletMapper.changeBalanceAndUnconfimed(oasDetail.getUserUuid(),value,userWallet.getUnconfirmedBalance().add(oasDetail.getValue()),now)>0?ErrorCode.SUCCESS:ErrorCode.UPDATE_FAILED;
  }
  
  public List<OasDetailResp> getWithdrawList(){
	  return oasDetailMapper.getAllWithdrawRecord();
  }
  
  public ErrorCode setWithdrawResult(String uuid,Integer result) {
	  OasDetail detail = oasDetailMapper.getRecordByUuid(uuid);
	  if(detail == null) {
		  return ErrorCode.SELECT_EMPTY;
	  }
	  if(detail.getStatus()!=0) {
		  return ErrorCode.OAS_EVENT_HAVE_HANDLED;
	  }
	  //BigDecimal extra = detail.getExtra(); //手续费
	  BigDecimal value = detail.getValue(); //提币金额
	  
	  //查询system账号
	  UserModel systemInfo = oasDetailMapper.getSystemUserInfo();
	  if(systemInfo!=null) {
		  String now = DateUtils.dateTimeNow();
		  //system的在线钱包
		  UserWallet systemWallet = userWalletMapper.selectByUserUuid(systemInfo.getUuid());
		  //用户的钱包
		  UserWallet userWallet = userWalletMapper.selectByUserUuid(detail.getUserUuid());
		  if(systemWallet == null || userWallet == null) {
			  return ErrorCode.WALLET_ONLINE_NOT_EXIST;
		  }
		  //管理员拒绝该提币请求
		  if(result == 2) {
			  //待交易记录减去value，代币加value
			  if(userWallet.getUnconfirmedBalance().compareTo(value) == -1) {
				  return ErrorCode.BALANCE_NOT_ENOUGH;
			  }
			 
			  Integer tResult = userWalletMapper.changeBalanceAndUnconfimed(detail.getUserUuid(),userWallet.getBalance().add(value),userWallet.getUnconfirmedBalance().subtract(value),now);
			  //Integer sResult = userWalletMapper.increaseBalance(systemWallet.getUuid(), extra);
			  if(tResult == 0 ) {
				  return ErrorCode.UPDATE_FAILED;
			  }
		  }else {
			  //最初已减掉钱包balance，和手续费，无需再减
			 /* if(userWallet.getBalance().compareTo(extra) == -1) {
				  return ErrorCode.OAS_EXTRA_MONEY_NOT_ENOUGH;
			  }*/
			  if(userWallet.getUnconfirmedBalance().compareTo(value) == -1) {
				  return ErrorCode.BALANCE_NOT_ENOUGH;
			  }
			  Integer tResult = userWalletMapper.changeBalanceAndUnconfimed(detail.getUserUuid(),userWallet.getBalance(),userWallet.getUnconfirmedBalance().subtract(value),now);
			  Integer sResult = userWalletMapper.increaseBalance(systemWallet.getUuid(), value);
			  if(tResult == 0 || sResult == 0) {
				  return ErrorCode.UPDATE_FAILED;
			  }
			  //增加在线钱包记录
			  UserCoin tokenCoin = ethWalletService.getUserCoin(detail.getUserUuid());
			  this.addDetail(userWallet, tokenCoin.getAddress(), UserWalletDetailScope.COIN_TO_ETH, value, detail.getRemark(), detail.getRemark());
			  
			  //操作交易钱包
			  BigInteger gasPrice =Convert.toWei(BigDecimal.valueOf(3), Convert.Unit.GWEI).toBigInteger();
			  BigInteger gasLimit = BigInteger.valueOf(60000);
			  
			  ethWalletService.systemTransfer(systemInfo.getUuid(),tokenCoin.getAddress(), userModelMapper.selectByUuid(detail.getUserUuid()).getName(),tokenCoin,value,gasPrice,gasLimit,detail.getRemark());
			  
		  }
		  return oasDetailMapper.setWithdrawResultByUuid(uuid,result,now)>0?ErrorCode.SUCCESS:ErrorCode.UPDATE_FAILED;
	  }else {
		  return ErrorCode.SYSTEM_NOT_EXIST;
	  }
  }
  
  public String getOasExtra() {
	  return oasDetailMapper.getOasExtra();
  }
  public ErrorCode updateOasExtra(String value) {
	  String now = DateUtils.dateTimeNow();
	  return oasDetailMapper.updateOasExtra(value,now)>0?ErrorCode.SUCCESS:ErrorCode.UPDATE_FAILED;
  }
  
}
