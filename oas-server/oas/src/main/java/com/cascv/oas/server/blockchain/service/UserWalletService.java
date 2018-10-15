package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import com.amazonaws.services.sns.model.PublishResult;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.constant.OasEventEnum;
import com.cascv.oas.server.blockchain.mapper.EthWalletMapper;
import com.cascv.oas.server.blockchain.mapper.OasDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.model.OasDetail;
import com.cascv.oas.server.blockchain.model.OasDetailResp;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.user.mapper.UserModelMapper;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.MessageService;

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
  private TimeZoneService timeZoneService;
  @Autowired
  private EthWalletService ethWalletService;
  
  @Autowired
  private EthWalletMapper ethWalletMapper;
  
  @Autowired
  private MessageService messageService;
 
/*  @Autowired
  private CoinClient coinClient;*/
  
  public UserWallet find(String userUuid){
    return userWalletMapper.selectByUserUuid(userUuid);
  }
  
  public static UserWalletDetail setDetail(UserWallet userWallet, String changeUserName, UserWalletDetailScope userWalletDetailScope, BigDecimal value, String comment, String remark,String oasDetailUuid) {
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
		  userWalletDetail.setOasDetailUuid(oasDetailUuid);
	      break;
	  case 3:
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle()+changeUserName);
	      break;
	  case 4:
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle()+changeUserName);
	      break;
	  case 5:
		  log.info("充币");
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle());
		  userWalletDetail.setTxResult(1);
		  userWalletDetail.setOasDetailUuid(oasDetailUuid);
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
	  return userWalletDetail;
  }
  
  private void addDetail(UserWallet userWallet, String changeUserName, UserWalletDetailScope userWalletDetailScope, BigDecimal value, String comment, String remark) {
	  UserWalletDetail userWalletDetail = setDetail(userWallet,  changeUserName,  userWalletDetailScope,  value,  comment, remark,null);
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
	  String oasUuid = UuidUtils.getPrefixUUID(UuidPrefix.OAS_DETAIL);
	  oasDetail.setUuid(oasUuid);
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
	  }else {
		  return ErrorCode.SYSTEM_NOT_EXIST;
	  }
	  Integer userResut =  userWalletMapper.changeBalanceAndUnconfimed(oasDetail.getUserUuid(),value,userWallet.getUnconfirmedBalance().add(oasDetail.getValue()),now);
	  if(userResut == 0) {
		  return ErrorCode.UPDATE_FAILED;
	  }
	  //TODO 提币插入在线钱包交易记录，将oasId插入在线钱包明细表
	  userWalletDetailMapper.insertSelective(setDetail(userWallet, "", UserWalletDetailScope.COIN_TO_ETH, oasDetail.getValue(), oasDetail.getRemark(), oasDetail.getRemark(),oasUuid));
	  
	  UserModel adminInfo = oasDetailMapper.getAdminUserInfo();
	  if(adminInfo!=null && adminInfo.getMobile()!=null) {
		  String mobile = "+86".concat(adminInfo.getMobile());
		  String SIGNNAME = "国科云景";
		  String content = "【"+SIGNNAME+"】"+"报告管理员：有一条新的提币记录！";
	      PublishResult publishResult = messageService.sendSMSMessage(mobile,content);
	      log.info(publishResult.toString());
	      if(publishResult.getMessageId() == null) {
	    	  return ErrorCode.SEND_SMS_ERROR;
		  }
	  }
	 
	  return ErrorCode.SUCCESS;
  }
  
  public List<OasDetailResp> getWithdrawList(){
	  
	  List<OasDetailResp> OasDetailRespList=oasDetailMapper.getAllWithdrawRecord();
	  for(OasDetailResp OasDetailResp : OasDetailRespList)
		{
			String srcFormater="yyyy-MM-dd HH:mm:ss";
			String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String created=DateUtils.string2Timezone(srcFormater, OasDetailResp.getCreated(), dstFormater, dstTimeZoneId);
			OasDetailResp.setCreated(created);
			log.info("newCreated={}",created);
		}
	  return OasDetailRespList;
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
		  String hash = null;
		  String now = DateUtils.dateTimeNow();
		  //system的在线钱包
		  UserWallet systemWallet = userWalletMapper.selectByUserUuid(systemInfo.getUuid());
		  //用户的钱包
		  UserWallet userWallet = userWalletMapper.selectByUserUuid(detail.getUserUuid());
		  if(systemWallet == null || userWallet == null) {
			  return ErrorCode.NO_ONLINE_ACCOUNT;
		  }
		  //管理员拒绝该提币请求
		  if(result == 2) {
			  ErrorCode resultErrorReturn = errorOperate(userWallet,value,detail,now);
			  if(resultErrorReturn.getCode()!=0) {
				  return resultErrorReturn;
			  }
		  }else {		
			  
			  //UserCoin tokenCoin = ethWalletService.getUserCoin(detail.getUserUuid());
			  UserCoin tokenCoin = ethWalletService.getUserCoin(systemInfo.getUuid()); //system的usercoin
			  
			  //操作交易钱包
			  BigInteger gasPrice =Convert.toWei(BigDecimal.valueOf(3), Convert.Unit.GWEI).toBigInteger();
			  BigInteger gasLimit = BigInteger.valueOf(60000);
			  //获取当前用户的eth wallet
			  EthWallet ethWallet = ethWalletMapper.selectByUserUuid(detail.getUserUuid());
			 if(ethWallet == null) {return ErrorCode.NO_ETH_WALLET;}
			  
			 String myName = userModelMapper.selectByUuid(detail.getUserUuid()).getName();
			 ReturnValue<String> ethInfo = ethWalletService.systemTransfer(true,systemInfo.getUuid(),ethWallet.getAddress(),myName,tokenCoin,value,gasPrice,gasLimit,detail.getRemark());
			 if(ethInfo == null || ethInfo.getData()==null) {
				 oasDetailMapper.updateStatusByUuid(detail.getUuid(),OasEventEnum.FAILED.getCode());
				 errorOperate(userWallet,value,detail,now);
				 return ErrorCode.ETH_RETURN_HASH;
			 }
			 hash = ethInfo.getData();
			 
			  //最初已减掉钱包balance，和手续费，无需再减
			 /* if(userWallet.getBalance().compareTo(extra) == -1) {
				  return ErrorCode.OAS_EXTRA_MONEY_NOT_ENOUGH;
			  }*/
			 if(userWallet.getUnconfirmedBalance().compareTo(value) == -1) {
				  return ErrorCode.UNCONFIRMED_BALANCE;
			  }
			 
			 Integer tResult = userWalletMapper.changeBalanceAndUnconfimed(detail.getUserUuid(),userWallet.getBalance(),userWallet.getUnconfirmedBalance(),now);//.subtract(value)
			 Integer sResult = userWalletMapper.increaseBalance(systemWallet.getUuid(), value);
			 if(tResult == 0 || sResult == 0) {
				 return ErrorCode.UPDATE_FAILED;
			 }
			//增加在线钱包记录换到提币请求时
			 //userWalletDetailMapper.insertSelective(setDetail(userWallet, tokenCoin.getAddress(), UserWalletDetailScope.COIN_TO_ETH, value, detail.getRemark(), detail.getRemark(),hash,coinClient.getNetName()));
			 userWalletDetailMapper.insertSelective(setDetail(systemWallet, myName, UserWalletDetailScope.TRANSFER_IN, value, detail.getRemark(), detail.getRemark(),null));//system转入
		  }
		  return oasDetailMapper.setWithdrawResultByUuid(uuid,result,now,hash)>0?ErrorCode.SUCCESS:ErrorCode.UPDATE_FAILED;
	  }else {
		  return ErrorCode.SYSTEM_NOT_EXIST;
	  }
  }
  //提币失败，待交易金额退回代币
  private ErrorCode errorOperate(UserWallet userWallet,BigDecimal value,OasDetail detail,String now) {
	  //待交易记录减去value，代币加value
	  if(userWallet.getUnconfirmedBalance().compareTo(value) == -1) {
		  return ErrorCode.UNCONFIRMED_BALANCE;
	  }
	 
	  Integer tResult = userWalletMapper.changeBalanceAndUnconfimed(detail.getUserUuid(),userWallet.getBalance().add(value),userWallet.getUnconfirmedBalance().subtract(value),now);
	  //Integer sResult = userWalletMapper.increaseBalance(systemWallet.getUuid(), extra);
	  if(tResult == 0 ) {
		  return ErrorCode.UPDATE_FAILED;
	  }
	  //提币失败更新在线钱包detail状态
	  Integer uwdResult = userWalletDetailMapper.updateByOasDetailUuid(2,detail.getUuid());
	  if(uwdResult == null) {
  		 return ErrorCode.UPDATE_FAILED;
  	  }
	  return ErrorCode.SUCCESS;
  }
  
  public String getOasExtra() {
	  return oasDetailMapper.getOasExtra();
  }
  public ErrorCode updateOasExtra(String value) {
	  String now = DateUtils.dateTimeNow();
	  return oasDetailMapper.updateOasExtra(value,now)>0?ErrorCode.SUCCESS:ErrorCode.UPDATE_FAILED;
  }
  
  
}
