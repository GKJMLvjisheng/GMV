package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import com.alibaba.druid.util.StringUtils;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
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
import com.cascv.oas.server.blockchain.model.OasReq;
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

  
  public static UserWalletDetail setDetail(UserWallet userWallet, String changeUserName, UserWalletDetailScope userWalletDetailScope, BigDecimal value, String comment, String remark,String oasDetailUuid,BigDecimal restBalance) {
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
		  userWalletDetail.setTxResult(1);
	      break;
	  case 2:
		  log.info("提币");
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle());
		  userWalletDetail.setOasDetailUuid(oasDetailUuid);
	      break;
	  case 3:
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle()+changeUserName);
		  userWalletDetail.setTxResult(1);
	      break;
	  case 4:
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle()+changeUserName);
		  userWalletDetail.setOasDetailUuid(oasDetailUuid);
		  userWalletDetail.setTxResult(1);
	      break;
	  case 5:
		  log.info("充币");
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle());
		  userWalletDetail.setTxResult(1);
		  userWalletDetail.setOasDetailUuid(oasDetailUuid);
	      break;
	  case 6:
		  log.info("购买矿机");
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle()+comment);
		  userWalletDetail.setTxResult(1);
		  break;
	  case 7:
		  log.info("矿机推广奖励");
		  userWalletDetail.setSubTitle(changeUserName+userWalletDetailScope.getSubTitle());
		  userWalletDetail.setTxResult(1);
		  break;
	  case 8:
		  log.info("矿机推广奖励");
		  userWalletDetail.setSubTitle(changeUserName+userWalletDetailScope.getSubTitle());
		  userWalletDetail.setTxResult(1);
		  break;
	  case 9:
		  userWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle());
		  userWalletDetail.setTxResult(1);
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
	  userWalletDetail.setRestBalance(restBalance==null?userWallet.getBalance():restBalance);
	  return userWalletDetail;
  }
  
  public void addDetail(UserWallet userWallet, String changeUserName, UserWalletDetailScope userWalletDetailScope, BigDecimal value, String comment, String remark,BigDecimal restBalance) {
	  UserWalletDetail userWalletDetail = setDetail(userWallet,  changeUserName,  userWalletDetailScope,  value,  comment, remark,null,restBalance);
	  log.info("comment={}", comment);
	  userWalletDetailMapper.insertSelective(userWalletDetail);
  }
  
  
  public UserWallet create(String userUuid){
    UserWallet userWallet = new UserWallet();
    userWallet.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.USER_WALLET));
    userWallet.setUserUuid(userUuid);
    userWallet.setBalance(BigDecimal.ZERO);
    String now = DateUtils.getTime();
    userWallet.setCreated(now);
    userWallet.setUpdated(now);
    userWalletMapper.deleteByUserUuid(userUuid);
    userWalletMapper.insertSelective(userWallet);
    return userWallet;
  }
  
  public UserWallet createAccountByMoney(String userUuid,BigDecimal balance){
	    UserWallet userWallet = new UserWallet();
	    userWallet.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.USER_WALLET));
	    userWallet.setUserUuid(userUuid);
	    userWallet.setBalance(balance);
	    String now = DateUtils.getTime();
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
    this.addDetail(fromUserWallet, toUserName, UserWalletDetailScope.TRANSFER_OUT, value, comment, remark,fromUserWallet.getBalance().subtract(value));
    
    userWalletMapper.increaseBalance(toUserWallet.getUuid(), value);
    this.addDetail(toUserWallet, fromUserName, UserWalletDetailScope.TRANSFER_IN, value,comment, remark,toUserWallet.getBalance().add(value));
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
	  this.addDetail(userWallet, changeUserName, UserWalletDetailScope.ENERGY_TO_COIN, token, point.toString(), "",userWallet.getBalance().add(token));
  }

  public ErrorCode withdraw(OasDetail oasDetail,UserModel user){
	//oasDetail.setExtra(new BigDecimal(oasDetailMapper.getOasExtra()));
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
	  String now = DateUtils.getTime();
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
		  //system手续费记录
		  userWalletDetailMapper.insertSelective(setDetail(systemWallet, user.getName(), UserWalletDetailScope.TRANSFER_IN, oasDetail.getExtra(), oasDetail.getRemark(), oasDetail.getRemark(),oasUuid,systemWallet.getBalance().add(oasDetail.getExtra())));
	  }else {
		  return ErrorCode.SYSTEM_NOT_EXIST;
	  }
	  Integer userResut =  userWalletMapper.changeBalanceAndUnconfimed(oasDetail.getUserUuid(),value,userWallet.getUnconfirmedBalance().add(oasDetail.getValue()),now);
	  if(userResut == 0) {
		  return ErrorCode.UPDATE_FAILED;
	  }
	  //TODO 提币插入在线钱包交易记录，将oasId插入在线钱包明细表
	  userWalletDetailMapper.insertSelective(setDetail(userWallet, "", UserWalletDetailScope.COIN_TO_ETH, oasDetail.getValue(), oasDetail.getRemark(), oasDetail.getRemark(),oasUuid,value));
	  
	 /* UserModel adminInfo = oasDetailMapper.getAdminUserInfo();
	  if(adminInfo!=null && adminInfo.getMobile()!=null) {
		  String mobile = "+86".concat(adminInfo.getMobile());
		  String SIGNNAME = "OASESCHAIN";
		  String content = "【"+SIGNNAME+"】"+"报告管理员：有一条新的提币记录！";
	      PublishResult publishResult = messageService.sendSMSMessage(mobile,content);
	      log.info("提币短信发送成功"+publishResult.toString());
	      if(publishResult.getMessageId() == null) {
	    	  return ErrorCode.SEND_SMS_ERROR;
		  }
	  }else {
		  log.info("管理员用户不存在或未设置电话");
	  }*/
	  return ErrorCode.SUCCESS;
  }
  
  public PageDomain<OasDetailResp> getWithdrawList(Integer pageNum, Integer pageSize){
	  PageDomain<OasDetailResp> result = new PageDomain<OasDetailResp>();
	  result.setPageNum(pageNum);
	  result.setPageSize(pageSize);
	  result.setOffset((pageNum - 1)*pageSize);
	  List<OasDetailResp> list = oasDetailMapper.getAllWithdrawRecord((pageNum - 1)*pageSize, pageSize);
	  if(list != null) {
		  for(OasDetailResp oasDetailResp : list)
			{
				String srcFormater="yyyy-MM-dd HH:mm:ss";
				String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, oasDetailResp.getCreated(), dstFormater, dstTimeZoneId);
				oasDetailResp.setCreated(created);
				log.info("newCreated={}",created);
			}
	  }
	  result.setRows(list);
	  result.setTotal(oasDetailMapper.getAllWithdrawRecordCount());
	  return result;
  }
  
  public ErrorCode setWithdrawResult(String uuid,Integer result) {
	  OasDetail detail = oasDetailMapper.getRecordByUuid(uuid);
	  if(detail == null) {
		  return ErrorCode.SELECT_EMPTY;
	  }
	  if(detail.getStatus()!=0) {
		  return ErrorCode.OAS_EVENT_HAVE_HANDLED;
	  }
	  BigDecimal extra = detail.getExtra(); //手续费
	  BigDecimal value = detail.getValue(); //提币金额
	  
	  //查询system账号
	  UserModel systemInfo = oasDetailMapper.getSystemUserInfo();
	  if(systemInfo!=null) {
		  String hash = null;
		  String now = DateUtils.getTime();
		  //system的在线钱包
		  UserWallet systemWallet = userWalletMapper.selectByUserUuid(systemInfo.getUuid());
		  //用户的钱包
		  UserWallet userWallet = userWalletMapper.selectByUserUuid(detail.getUserUuid());
		  if(systemWallet == null || userWallet == null) {
			  return ErrorCode.NO_ONLINE_ACCOUNT;
		  }
		  UserModel user = userModelMapper.selectByUuid(detail.getUserUuid());
		  //管理员拒绝该提币请求
		  if(result == 2) {
			  ErrorCode resultErrorReturn = errorOperate(userWallet,systemWallet,value,extra,detail,now,user);
			  if(resultErrorReturn.getCode()!=0) {
				  return resultErrorReturn;
			  }
		  }else {		
			  //UserCoin tokenCoin = ethWalletService.getUserCoin(detail.getUserUuid());
			  UserCoin tokenCoin = ethWalletService.getUserCoin(systemInfo.getUuid()); //system的usercoin
			  
			  //操作交易钱包
			  BigInteger gasPrice =Convert.toWei(BigDecimal.valueOf(5), Convert.Unit.GWEI).toBigInteger();
			  BigInteger gasLimit = BigInteger.valueOf(60000);
			  //获取当前用户的eth wallet
			  EthWallet ethWallet = ethWalletMapper.selectByUserUuid(detail.getUserUuid());
			 if(ethWallet == null) {
				 return ErrorCode.NO_ETH_WALLET;
			 }
			 //最初已减掉钱包balance，和手续费，无需再减
			 /* if(userWallet.getBalance().compareTo(extra) == -1) {
				  return ErrorCode.OAS_EXTRA_MONEY_NOT_ENOUGH;
			  }
			 if(userWallet.getUnconfirmedBalance().compareTo(value) == -1) {
				  return ErrorCode.UNCONFIRMED_BALANCE;
			 }*/
			  
			 String myName = userModelMapper.selectByUuid(detail.getUserUuid()).getName();
			 ReturnValue<String> ethInfo = ethWalletService.systemTransfer(true,systemInfo.getUuid(),ethWallet.getAddress(),myName,tokenCoin,value,gasPrice,gasLimit,detail.getRemark());
			 if(ethInfo == null || ethInfo.getData()==null) {
				 oasDetailMapper.updateStatusByUuid(detail.getUuid(),OasEventEnum.FAILED.getCode());
				 errorOperate(userWallet,systemWallet,value,extra,detail,now,user);
				 return ErrorCode.ETH_RETURN_HASH;
			 }
			 hash = ethInfo.getData();
			 
			 /* 移到后台确认转账成功进行操作
			  * Integer tResult = userWalletMapper.changeBalanceAndUnconfimed(detail.getUserUuid(),userWallet.getBalance(),userWallet.getUnconfirmedBalance(),now);//.subtract(value)
			 Integer sResult = userWalletMapper.increaseBalance(systemWallet.getUuid(), value);
			 if(tResult == 0 || sResult == 0) {
				 return ErrorCode.UPDATE_FAILED;
			 }*/
			//增加在线钱包记录换到提币请求时
			 //userWalletDetailMapper.insertSelective(setDetail(userWallet, tokenCoin.getAddress(), UserWalletDetailScope.COIN_TO_ETH, value, detail.getRemark(), detail.getRemark(),hash,coinClient.getNetName()));
			// userWalletDetailMapper.insertSelective(setDetail(systemWallet, myName, UserWalletDetailScope.TRANSFER_IN, value, detail.getRemark(), detail.getRemark(),null));//system转入
		  }
		  return oasDetailMapper.setWithdrawResultByUuid(uuid,result,now,hash)>0?ErrorCode.SUCCESS:ErrorCode.UPDATE_FAILED;
	  }else {
		  return ErrorCode.SYSTEM_NOT_EXIST;
	  } 
  }
  //提币失败，待交易金额退回代币，手续费从system退回
  private ErrorCode errorOperate(UserWallet userWallet,UserWallet systemWallet,BigDecimal value,BigDecimal extra,OasDetail detail,String now,UserModel user) {
	  //待交易记录减去value，代币加value
	  /*if(userWallet.getUnconfirmedBalance().compareTo(value) == -1) {
		  return ErrorCode.UNCONFIRMED_BALANCE;
	  }*/
/*	  if(systemWallet.getBalance().compareTo(extra) == -1) {
		  return ErrorCode.OAS_EXTRA_MONEY_NOT_ENOUGH;
	  }*/
	 
	  Integer tResult = userWalletMapper.changeBalanceAndUnconfimed(detail.getUserUuid(),userWallet.getBalance().add(value).add(extra),userWallet.getUnconfirmedBalance().subtract(value),now);
	  Integer sResult = userWalletMapper.decreaseBalance(systemWallet.getUuid(), extra);
	  if(tResult == 0 || sResult == 0) {
		  return ErrorCode.UPDATE_FAILED;
	  }
	  //提币失败更新在线钱包detail状态
	  Integer uwdResult = userWalletDetailMapper.updateByOasDetailUuid(2,detail.getUuid(),userWallet.getBalance().add(value).add(extra),0);
	  if(uwdResult == null) {
  		 return ErrorCode.UPDATE_FAILED;
  	  }
	  //插入system的手续费转出记录
	  //userWalletDetailMapper.updateByOasDetailUuid(2,detail.getUuid(),systemWallet.getBalance().subtract(extra),1);
	  userWalletDetailMapper.insertSelective(setDetail(systemWallet, (user==null?"":user.getName()), UserWalletDetailScope.TRANSFER_OUT, extra, detail.getRemark(), detail.getRemark(),detail.getUuid(),systemWallet.getBalance().subtract(extra)));
	  
	  return ErrorCode.SUCCESS;
  }
  
  public OasReq getOasExtra() {
	  String value = oasDetailMapper.getOasExtra("oas_extra");
	  String max = oasDetailMapper.getOasExtra("oas_max");
	  String min = oasDetailMapper.getOasExtra("oas_min");
	  OasReq req = new OasReq();
	  req.setValue(StringUtils.isEmpty(value)?"0":value);
	  req.setValueMax(StringUtils.isEmpty(max)?"0":max);
	  req.setValueMin(StringUtils.isEmpty(min)?"0":min);
	  return req;
  }
  public ErrorCode updateOasExtra(OasReq oasDetail) {
	  String now = DateUtils.getTime();
	  /*OasReq reqExist = this.getOasExtra();
	  if(reqExist.getValue().equals(oasDetail.getValue()) && reqExist.getValueMin().equals(oasDetail.getValueMin()) 
			  && reqExist.getValueMax().equals(oasDetail.getValueMax())) {
		  return ErrorCode.OAS_NO_USERFUL_UPDATE;
	  }*/
	  return oasDetailMapper.updateOasExtra(oasDetail,now)>0?ErrorCode.SUCCESS:ErrorCode.UPDATE_FAILED;
  }
  
  public ErrorCode setFirstOneUserBalance(UserWalletDetail detail) {
	  UserModel firstOne = oasDetailMapper.getFirstOneUserInfo();
	  if(firstOne ==null) {
		  return ErrorCode.FIRSTONE_NOT_EXIST;
	  }
	  userWalletMapper.changeBalanceAndUnconfimed(firstOne.getUuid(), detail.getValue(), null, DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS));
	  UserWallet firstOneWallet = userWalletMapper.getFirstOneWallet();
	  if(firstOneWallet == null) {
		  return ErrorCode.NO_ONLINE_ACCOUNT;
	  }
	  userWalletDetailMapper.insertSelective(setDetail(firstOneWallet,"",UserWalletDetailScope.FIRSTONE_UPDATE,detail.getValue(),detail.getRemark(),detail.getRemark(),null,firstOneWallet.getBalance()));
	  return ErrorCode.SUCCESS;
  }
  
  public void insertSystemInit(BigDecimal value) {
	//system的在线钱包
	  UserWallet systemWallet = userWalletMapper.getSystemWallet();
	  if(systemWallet!=null) {
		  userWalletDetailMapper.insertSelective(setDetail(systemWallet, "", UserWalletDetailScope.SYSTEM_INIT, value, UserWalletDetailScope.SYSTEM_INIT.getSubTitle(), UserWalletDetailScope.SYSTEM_INIT.getSubTitle(),null,systemWallet.getBalance()));
	  }  
	  
  }
  public void insertFirstOneInit(BigDecimal value) {
	  UserWallet firstOneWallet = userWalletMapper.getFirstOneWallet();
	  if(firstOneWallet!=null) {
		  userWalletDetailMapper.insertSelective(setDetail(firstOneWallet, "", UserWalletDetailScope.FIRSTONE_INIT, value, UserWalletDetailScope.FIRSTONE_INIT.getSubTitle(), UserWalletDetailScope.FIRSTONE_INIT.getSubTitle(),null,firstOneWallet.getBalance()));
			 
	  }
  }  
}
