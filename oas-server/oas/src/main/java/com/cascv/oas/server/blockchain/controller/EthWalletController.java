package com.cascv.oas.server.blockchain.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.utils.Convert;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.PageIODomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.mapper.EthWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.EthWalletTradeRecordMapper;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.model.EthWalletDetail;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.model.UserCoinResp;
import com.cascv.oas.server.blockchain.service.EthWalletDetailService;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.wrapper.BackupEthWallet;
import com.cascv.oas.server.blockchain.wrapper.EthWalletMultiTransfer;
import com.cascv.oas.server.blockchain.wrapper.EthWalletMultiTransferResp;
import com.cascv.oas.server.blockchain.wrapper.EthWalletSummary;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransfer;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransferResp;
import com.cascv.oas.server.blockchain.wrapper.PreferNetworkReq;
import com.cascv.oas.server.blockchain.wrapper.TimeLimitInfo;
import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;
import com.cascv.oas.server.log.annotation.WriteLog;
import com.cascv.oas.server.shiro.BaseShiroController;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping(value="/api/v1/ethWallet")
public class EthWalletController extends BaseShiroController {
  
  @Autowired
  private EthWalletService ethWalletService;
  @Autowired
  private EthWalletDetailService ethWalletDetailService;
  @Autowired
  private EthWalletDetailMapper ethWalletDetailMapper;
  @Autowired 
  private TimeZoneService timeZoneService;
  @Autowired
  private EthWalletTradeRecordMapper ethWalletTradeRecordMapper;
  
/*  @PostMapping(value="/selectContractSymbol")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> selectContractSymbol(@RequestBody SelectContractSymbolName selectContractSymbolName){
//	  UserModel userModel = ShiroUtils.getUser();
//	  List<ContractSymbol> conractSymbolList = ethWalletService.selectContractSymbol(userModel.getName());
	  String name = selectContractSymbolName.getName();
	  List<ContractSymbol> conractSymbolList = ethWalletService.selectContractSymbol(name);
	return new ResponseEntity.Builder<List<ContractSymbol>>()
			.setData(conractSymbolList)
			.setErrorCode(ErrorCode.SUCCESS)
			.build();
	  
  }*/

  @PostMapping(value="/transfer")
  @RequiresPermissions("交易钱包-转账")
  @ResponseBody
  @Transactional
  @WriteLog(value="transfer")
  public ResponseEntity<?> transfer(@RequestBody EthWalletTransfer ethWalletTransfer){
	  if(ethWalletTransfer.getToUserAddress().equals("0")) {
		  return new ResponseEntity.Builder<Integer>()
				  .setData(1)
				  .setErrorCode(ErrorCode.WRONG_ADDRESS)
				  .build();
	  }else if(ethWalletTransfer.getAmount().compareTo(BigDecimal.ZERO) == 0) {
		  return new ResponseEntity.Builder<Integer>()
				  .setData(1)
				  .setErrorCode(ErrorCode.WRONG_AMOUNT)
				  .build();
	  }else {
		  BigInteger gasPrice = ethWalletTransfer.getGasPrice();
		  if (gasPrice == null)
			  gasPrice=Convert.toWei(BigDecimal.valueOf(3), Convert.Unit.GWEI).toBigInteger();
		  else {
			//前端传的值单位从wei改为Gwei，差额为10的9次方
			  BigInteger k = new BigInteger("10");
			  int m = new Integer("9");
			  BigInteger price = k.pow(m);
			  gasPrice = gasPrice.multiply(price);
		  }
		  BigInteger gasLimit =  ethWalletTransfer.getGasLimit();
	      if (gasLimit == null)
	    	  gasLimit = BigInteger.valueOf(60000);
	      
	      ReturnValue<String> returnValue=ethWalletService.transfer(
	        ShiroUtils.getUserUuid(),
	        ethWalletTransfer.getToUserAddress(),
	        ethWalletTransfer.getAmount(),gasPrice,gasLimit, 
	        ethWalletTransfer.getRemark());	  
      EthWalletTransferResp resp = new EthWalletTransferResp();
      resp.setTxHash(returnValue.getData());
	    return new ResponseEntity.Builder<EthWalletTransferResp>()
	        .setData(resp)
	        .setErrorCode(returnValue.getErrorCode())
	        .build();
	  }
  }

  @PostMapping(value="/multiTtransfer")
  @ResponseBody
  @Transactional
  @WriteLog(value="multiTransfer")
  public ResponseEntity<?> multiTtransfer(@RequestBody EthWalletMultiTransfer ethWalletMultiTransfer){
		  
		//前端传的值单位从wei改为Gwei，差额为10的9次方
	      BigInteger gasPrice = ethWalletMultiTransfer.getGasPrice();
	      if (gasPrice == null)
				gasPrice = Convert.toWei(BigDecimal.valueOf(6), Convert.Unit.GWEI).toBigInteger();
	      else {
	    	  BigInteger k = new BigInteger("10");
			  int m = new Integer("9");
			  BigInteger price = k.pow(m);  
			  gasPrice = ethWalletMultiTransfer.getGasPrice().multiply(price);
	      }
		  
		  BigInteger gasLimit = ethWalletMultiTransfer.getGasLimit();
		  if (gasLimit == null)
			  gasLimit = BigInteger.valueOf(600000);
		  
		  ReturnValue<String> returnValue=ethWalletService.multiTransfer(
	        ShiroUtils.getUserUuid(),
          ethWalletMultiTransfer.getQuota(), gasPrice, gasLimit, "");
      EthWalletMultiTransferResp resp = new EthWalletMultiTransferResp();
      resp.setTxHash(returnValue.getData());
	    return new ResponseEntity.Builder<EthWalletMultiTransferResp>()
	        .setData(resp)
	        .setErrorCode(returnValue.getErrorCode())
	        .build();
  }
  
  @PostMapping(value="/listCoin")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> listCoin(){
    String userUuid = ShiroUtils.getUserUuid();
    List<UserCoin> userCoinList = ethWalletService.listCoin(userUuid);

    UserCoinResp listDouble = new UserCoinResp();
    listDouble.setUserCoin(userCoinList);
    if(userCoinList!=null && userCoinList.size()>0) {
    	List<UserCoin> noUserCoinList = new ArrayList<>();
    	noUserCoinList.add(ethWalletService.getEthCoinTemporary(userCoinList.get(0)));
    	listDouble.setNoShowCoin(noUserCoinList);
    }
    return new ResponseEntity.Builder<UserCoinResp>()
            .setData(listDouble)
            .setErrorCode(ErrorCode.SUCCESS)
            .build();
  }

  @PostMapping(value="/transactionDetail")
  @ResponseBody()
  public ResponseEntity<?> transactionDetail(@RequestBody PageIODomain<Integer> pageInfo){
    Integer pageNum = pageInfo.getPageNum();
    Integer pageSize = pageInfo.getPageSize();
    Integer limit = pageSize;
    Integer offset;
    
    if (limit == null)
      limit = 10;
    if (pageNum != null && pageNum > 0)
      offset = (pageNum - 1) * limit;
    else 
      offset = 0;
    EthWallet ethWallet = ethWalletService.getEthWalletByUserUuid(ShiroUtils.getUserUuid());
    Integer inOrOut;
    log.info("inOrOut{}",pageInfo.getInOrOut());
    if(pageInfo.getInOrOut()!=null) {
    inOrOut=pageInfo.getInOrOut();
    List<EthWalletDetail> ethWalletDetailList = ethWalletDetailService.selectByInOrOut(
            ethWallet.getAddress(), offset,limit,inOrOut);
    Integer count = ethWalletDetailList.size();
    PageDomain<EthWalletDetail> pageEthWalletDetail= new PageDomain<>();
    pageEthWalletDetail.setTotal(count);
    pageEthWalletDetail.setAsc("desc");
    pageEthWalletDetail.setOffset(offset);
    pageEthWalletDetail.setPageNum(pageNum);
    pageEthWalletDetail.setPageSize(pageSize);
    pageEthWalletDetail.setRows(ethWalletDetailList);
    log.info("****转入/转出****");
    return new ResponseEntity.Builder<PageDomain<EthWalletDetail>>()
    	    .setData(pageEthWalletDetail)
    	    .setErrorCode(ErrorCode.SUCCESS)
    	    .build();
    }else
    {
    List<EthWalletDetail> ethWalletDetailList = ethWalletDetailService.selectByPage(
        ethWallet.getAddress(), offset,limit);
    Integer count = ethWalletDetailMapper.selectCount(ethWallet.getAddress());
    PageDomain<EthWalletDetail> pageEthWalletDetail= new PageDomain<>();
    pageEthWalletDetail.setTotal(count);
    pageEthWalletDetail.setAsc("desc");
    pageEthWalletDetail.setOffset(offset);
    pageEthWalletDetail.setPageNum(pageNum);
    pageEthWalletDetail.setPageSize(pageSize);
    pageEthWalletDetail.setRows(ethWalletDetailList);
    log.info("****全部****");
    return new ResponseEntity.Builder<PageDomain<EthWalletDetail>>()
            .setData(pageEthWalletDetail)
            .setErrorCode(ErrorCode.SUCCESS)
            .build();
    }
  }
  
  	
  @PostMapping(value="/summary")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> summary(){
    ErrorCode errorCode=ErrorCode.SUCCESS;
    UserCoin tokenCoin = ethWalletService.getUserCoin(ShiroUtils.getUserUuid());
    
    EthWalletSummary ethWalletSummary = new EthWalletSummary();
    ethWalletSummary.setTotalTransaction(BigDecimal.ZERO);

    if (tokenCoin != null) {
      UserCoin ethCoin = ethWalletService.getEthCoinTemporary(tokenCoin);
      if(ethCoin!=null) {
    	  ethWalletSummary.setTotalBalance(tokenCoin.getBalance().add(ethCoin.getBalance()));
          ethWalletSummary.setTotalValue(tokenCoin.getValue().add(ethCoin.getValue()));
      }else {
          ethWalletSummary.setTotalBalance(tokenCoin.getBalance());
          ethWalletSummary.setTotalValue(tokenCoin.getValue());
      }
    } else {
      ethWalletSummary.setTotalBalance(BigDecimal.ZERO);
      ethWalletSummary.setTotalValue(BigDecimal.ZERO);
    }
    return new ResponseEntity.Builder<EthWalletSummary>()
        .setData(ethWalletSummary)
        .setErrorCode(errorCode)
        .build();
  }
  
  @PostMapping(value="/listNetwork")
  @ResponseBody
  public ResponseEntity<?> listNetwork(){
  Map<String,Object> map = new HashMap<>();
  
	Set<String> networks = ethWalletService.listNetwork();
	if (networks == null) {
		return new ResponseEntity.Builder<Map<String,Object>>()
		        .setData(map)
		        .setErrorCode(ErrorCode.NO_BLOCKCHAIN_NETWORK)
		        .build();
	} else {
	  map.put("network_list", networks);
	  map.put("network_active", ethWalletService.getActiveNet());
		return new ResponseEntity.Builder<Map<String,Object>>()
		        .setData(map)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
	}
  }
  
  @PostMapping(value="/setPreferNetwork")
  @ResponseBody
  @Transactional
  @RequiresRoles("admin")
  public ResponseEntity<?> setPreferNetwork(@RequestBody PreferNetworkReq req){
    ErrorCode errorCode=ethWalletService.setPreferNetwork(req.getPreferNetwork());
    return new ResponseEntity.Builder<Integer>()
        .setData(1)
        .setErrorCode(errorCode)
        .build();
  }
  
  
  
  /**
   * @author Ming Yang
   * @return 交易钱包交易明细接口
   */
  @PostMapping(value="/inqureEthWalletTradeRecord")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> inqureEthWalletTradeRecord(){
	  List<EthWalletTradeRecordInfo> ethWalletTradeRecords=ethWalletTradeRecordMapper.selectAllTradeRecord();
	  for (EthWalletTradeRecordInfo ethWalletTradeRecordInfo : ethWalletTradeRecords) {
			String srcFormater="yyyy-MM-dd HH:mm:ss";
			String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String created=DateUtils.string2Timezone(srcFormater, ethWalletTradeRecordInfo.getCreated(), dstFormater, dstTimeZoneId);
			ethWalletTradeRecordInfo.setCreated(created);
			log.info("newCreated={}",created);
		  }
		return new ResponseEntity.Builder<List<EthWalletTradeRecordInfo>>()
		        .setData(ethWalletTradeRecords)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
  }
  
  /**
   * @author Ming Yang
   * @return 交易钱包个人转入统计
   */
  @PostMapping(value="/inqureEthWalletInTotalTradeRecord")
  @ResponseBody
  @Transactional

  public ResponseEntity<?> inqureEthWalletInTotalTradeRecord(@RequestBody TimeLimitInfo timeLimitInfo){
	  
	  //获取当月第一天
	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	  Calendar c = Calendar.getInstance();
	  c.add(Calendar.MONTH, 0);
	  c.set(Calendar.DAY_OF_MONTH,1);
	  String nowMonthOfFirstDay =format.format(c.getTime());
      log.info("monthOfFirstDay:{}",nowMonthOfFirstDay);
      
      //获取当前年月日
      Date d = new Date();
      String nowDate = format.format(d);
      log.info("nowDate:{}",nowDate);
      
	  String startTime=timeLimitInfo.getStartTime();
	  String endTime=timeLimitInfo.getEndTime();
	  
	  if(startTime=="") {
		  startTime=nowMonthOfFirstDay;
	  }else {
		  startTime=timeLimitInfo.getStartTime();
	  }
	  if(endTime=="") {
		  endTime=nowDate;
	  }else {
		  endTime=timeLimitInfo.getEndTime();
	  }
	  
	  List<WalletTotalTradeRecordInfo> ethWalletInTotalTradeRecords=ethWalletTradeRecordMapper.selectAllInTotalTradeRecord(startTime, endTime);
		return new ResponseEntity.Builder<List<WalletTotalTradeRecordInfo>>()
		        .setData(ethWalletInTotalTradeRecords)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
  }
  /**
   * @author Ming Yang
   * @return 交易钱包个人转出统计
   */
  @PostMapping(value="/inqureEthWalletOutTotalTradeRecord")
  @ResponseBody
  @Transactional

  public ResponseEntity<?> inqureEthWalletOutTotalTradeRecord(@RequestBody TimeLimitInfo timeLimitInfo){
	  
	  //获取当月第一天
	  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	  Calendar c = Calendar.getInstance();
	  c.add(Calendar.MONTH, 0);
	  c.set(Calendar.DAY_OF_MONTH,1);
	  String nowMonthOfFirstDay =format.format(c.getTime());
      log.info("monthOfFirstDay:{}",nowMonthOfFirstDay);
      
      //获取当前年月日
      Date d = new Date();
      String nowDate = format.format(d);
      log.info("nowDate={}",nowDate);
      
	  String startTime=timeLimitInfo.getStartTime();
	  String endTime=timeLimitInfo.getEndTime();
	  
	  if(startTime=="") {
		  startTime=nowMonthOfFirstDay;
	  }else {
		  startTime=timeLimitInfo.getStartTime();
	  }
	  if(endTime=="") {
		  endTime=nowDate;
	  }else {
		  endTime=timeLimitInfo.getEndTime();
	  }
	  
	  List<WalletTotalTradeRecordInfo> ethWalletOutTotalTradeRecords=ethWalletTradeRecordMapper.selectAllOutTotalTradeRecord(startTime, endTime);
		return new ResponseEntity.Builder<List<WalletTotalTradeRecordInfo>>()
		        .setData(ethWalletOutTotalTradeRecords)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
  }
  /**
   * 充币请求申请
   * @param info
   * @return
   */
  @PostMapping(value="/reverseWithdraw")
  @RequiresPermissions("充币")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> reverseWithdraw(@RequestBody EthWalletTransfer info){
	  UserModel user = ShiroUtils.getUser();
	  if(user == null) {
		  return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(ErrorCode.USER_NOT_EXISTS).build();
	  }
	  if(info.getAmount()== null || info.getAmount().compareTo(BigDecimal.ZERO) ==0) {
		  return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(ErrorCode.VALUE_CAN_NOT_BE_NULL).build();
	  }
	  info.setToUserAddress(user.getName());
	  BigInteger gasPrice = info.getGasPrice();
	  if (gasPrice == null) {
		  gasPrice=Convert.toWei(BigDecimal.valueOf(3), Convert.Unit.GWEI).toBigInteger();
	  }else {
		//前端传的值单位从wei改为Gwei，差额为10的9次方
		  BigInteger k = new BigInteger("10");
		  int m = new Integer("9");
		  BigInteger price = k.pow(m);
		  gasPrice = gasPrice.multiply(price);
	  }
	  info.setGasPrice(gasPrice);
	  BigInteger gasLimit =  info.getGasLimit();
      if (gasLimit == null)
    	  gasLimit = BigInteger.valueOf(60000);
      info.setGasLimit(gasLimit);
	  return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(ethWalletService.reverseWithdraw(info,user)).build();
  }
  
  
  @PostMapping(value="/backupEthWallet")
  @ResponseBody
  public ResponseEntity<?> backupEthWallet(){
	  BackupEthWallet ethWallet = new BackupEthWallet();
	  UserModel user = ShiroUtils.getUser();
	  if(user == null) {
		  return new ResponseEntity.Builder<BackupEthWallet>()
				  .setData(ethWallet)
				  .setErrorCode(ErrorCode.USER_NOT_EXISTS).build();
	  }
	  ErrorCode errorCode = ethWalletService.backupEthWallet(user.getUuid(), ethWallet);
	  return new ResponseEntity.Builder<BackupEthWallet>()
			  .setData(ethWallet)
			  .setErrorCode(errorCode)
			  .build();
  }

  /**
      *  点击交易钱包记录查看区块链上转账记录，并标记状态
   * @param detail
   * @return
   */
 /* @PostMapping(value="/getExchangeResult")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> getExchangeResult(@RequestBody EthWalletDetail detail){
	  if(detail.getUuid() == null) {
		  return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(ErrorCode.SELECT_EMPTY).build();
	  }
	  return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(ethWalletService.getExchangeResult(detail)).build();
	  
  }
  */
}
