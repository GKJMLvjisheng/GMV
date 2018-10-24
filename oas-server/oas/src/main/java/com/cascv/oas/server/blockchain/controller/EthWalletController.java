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
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.blockchain.service.EthWalletDetailService;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.wrapper.BackupEthWallet;
import com.cascv.oas.server.blockchain.wrapper.EthWalletMultiTransfer;
import com.cascv.oas.server.blockchain.wrapper.EthWalletMultiTransferResp;
import com.cascv.oas.server.blockchain.wrapper.EthWalletStatus;
import com.cascv.oas.server.blockchain.wrapper.EthWalletSummary;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransfer;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransferResp;
import com.cascv.oas.server.blockchain.wrapper.PreferNetworkReq;
import com.cascv.oas.server.blockchain.wrapper.TimeLimitInfo;
import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;
import com.cascv.oas.server.energy.vo.EnergyWalletBalanceRecordInfo;
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
  //@RequiresRoles("admin")
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
  public ResponseEntity<?> inqureEthWalletTradeRecord(@RequestBody PageDomain<Integer> pageInfo){
	  
  	Integer pageNum = pageInfo.getPageNum();
    Integer pageSize = pageInfo.getPageSize();
    Integer limit = pageSize;
    Integer offset;

    if (pageSize == 0) {
      limit = 10;
    }
    if (pageNum != null && pageNum > 0)
    	offset = (pageNum - 1) * limit;
    else 
    	offset = 0;
    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	  if(searchValue != null) {
		  List<EthWalletTradeRecordInfo> ethWalletTradeRecordList=ethWalletTradeRecordMapper.selectAllTradeRecordBySearchValue(offset, limit, searchValue);
		  for (EthWalletTradeRecordInfo ethWalletTradeRecordInfo : ethWalletTradeRecordList) {
				String srcFormater="yyyy-MM-dd HH:mm:ss";
				String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, ethWalletTradeRecordInfo.getCreated(), dstFormater, dstTimeZoneId);
				ethWalletTradeRecordInfo.setCreated(created);
				log.info("newCreated={}",created);
			  }
		  PageDomain<EthWalletTradeRecordInfo> ethWalletTradeRecordInfo = new PageDomain<>();
		  Integer count=ethWalletTradeRecordMapper.countByTradeRecordBySearchValue(searchValue);
			  ethWalletTradeRecordInfo.setTotal(count);
			  ethWalletTradeRecordInfo.setAsc("desc");
			  ethWalletTradeRecordInfo.setOffset(offset);
			  ethWalletTradeRecordInfo.setPageNum(pageNum);
			  ethWalletTradeRecordInfo.setPageSize(pageSize);
			  ethWalletTradeRecordInfo.setRows(ethWalletTradeRecordList);
			return new ResponseEntity.Builder<PageDomain<EthWalletTradeRecordInfo>>()
			        .setData(ethWalletTradeRecordInfo)
			        .setErrorCode(ErrorCode.SUCCESS)
			        .build();
	  }else {
		  List<EthWalletTradeRecordInfo> ethWalletTradeRecordList=ethWalletTradeRecordMapper.selectAllTradeRecord(offset, limit);
		  for (EthWalletTradeRecordInfo ethWalletTradeRecordInfo : ethWalletTradeRecordList) {
				String srcFormater="yyyy-MM-dd HH:mm:ss";
				String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, ethWalletTradeRecordInfo.getCreated(), dstFormater, dstTimeZoneId);
				ethWalletTradeRecordInfo.setCreated(created);
				log.info("newCreated={}",created);
			  }
		  PageDomain<EthWalletTradeRecordInfo> ethWalletTradeRecordInfo = new PageDomain<>();
		  Integer count=ethWalletTradeRecordMapper.countByTradeRecord();
			  ethWalletTradeRecordInfo.setTotal(count);
			  ethWalletTradeRecordInfo.setAsc("desc");
			  ethWalletTradeRecordInfo.setOffset(offset);
			  ethWalletTradeRecordInfo.setPageNum(pageNum);
			  ethWalletTradeRecordInfo.setPageSize(pageSize);
			  ethWalletTradeRecordInfo.setRows(ethWalletTradeRecordList);
			return new ResponseEntity.Builder<PageDomain<EthWalletTradeRecordInfo>>()
			        .setData(ethWalletTradeRecordInfo)
			        .setErrorCode(ErrorCode.SUCCESS)
			        .build();
	  }
  }
  
  /**
   * @author Ming Yang
   * @return 交易钱包个人转入统计
   */
  @PostMapping(value="/inqureEthWalletInTotalTradeRecord")
  @ResponseBody
  @Transactional

  public ResponseEntity<?> inqureEthWalletInTotalTradeRecord(@RequestBody PageDomain<Integer> pageInfo){
  	
	Integer pageNum = pageInfo.getPageNum();
    Integer pageSize = pageInfo.getPageSize();
    Integer limit = pageSize;
    Integer offset;

    if (pageSize == 0) {
      limit = 10;
    }
    if (pageNum != null && pageNum > 0)
    	offset = (pageNum - 1) * limit;
    else 
    	offset = 0;
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
      
      String startTime=pageInfo.getStartTime();
      String endTime=pageInfo.getEndTime();
	  if(startTime=="")
		  startTime=nowMonthOfFirstDay;
	  if(endTime=="")
		  endTime=nowDate;
	  String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	  if(searchValue != null) {
		  List<WalletTotalTradeRecordInfo> ethWalletInTotalTradeRecordList=ethWalletTradeRecordMapper.selectAllInTotalTradeRecordBySearchValue(startTime, endTime, offset, limit, searchValue);
		  PageDomain<WalletTotalTradeRecordInfo> walletInTotalTradeRecordInfo = new PageDomain<>();
		  Integer count=ethWalletTradeRecordMapper.countByInTotalTradeRecordBySearchValue(startTime, endTime, searchValue);
		  walletInTotalTradeRecordInfo.setTotal(count);
		  walletInTotalTradeRecordInfo.setAsc("desc");
		  walletInTotalTradeRecordInfo.setOffset(offset);
		  walletInTotalTradeRecordInfo.setPageNum(pageNum);
		  walletInTotalTradeRecordInfo.setPageSize(pageSize);
		  walletInTotalTradeRecordInfo.setRows(ethWalletInTotalTradeRecordList);
			return new ResponseEntity.Builder<PageDomain<WalletTotalTradeRecordInfo>>()
			        .setData(walletInTotalTradeRecordInfo)
			        .setErrorCode(ErrorCode.SUCCESS)
			        .build();
	  }else {
		  List<WalletTotalTradeRecordInfo> ethWalletInTotalTradeRecordList=ethWalletTradeRecordMapper.selectAllInTotalTradeRecord(startTime, endTime,offset, limit);
		  PageDomain<WalletTotalTradeRecordInfo> walletInTotalTradeRecordInfo = new PageDomain<>();
		  Integer count=ethWalletTradeRecordMapper.countByInTotalTradeRecord(startTime, endTime);
		  walletInTotalTradeRecordInfo.setTotal(count);
		  walletInTotalTradeRecordInfo.setAsc("desc");
		  walletInTotalTradeRecordInfo.setOffset(offset);
		  walletInTotalTradeRecordInfo.setPageNum(pageNum);
		  walletInTotalTradeRecordInfo.setPageSize(pageSize);
		  walletInTotalTradeRecordInfo.setRows(ethWalletInTotalTradeRecordList);
			return new ResponseEntity.Builder<PageDomain<WalletTotalTradeRecordInfo>>()
			        .setData(walletInTotalTradeRecordInfo)
			        .setErrorCode(ErrorCode.SUCCESS)
			        .build();
	  }
	 
  }
  /**
   * @author Ming Yang
   * @return 交易钱包个人转出统计
   */
  @PostMapping(value="/inqureEthWalletOutTotalTradeRecord")
  @ResponseBody
  @Transactional

  public ResponseEntity<?> inqureEthWalletOutTotalTradeRecord(@RequestBody PageDomain<Integer> pageInfo){
  	
	Integer pageNum = pageInfo.getPageNum();
    Integer pageSize = pageInfo.getPageSize();
    Integer limit = pageSize;
    Integer offset;

    if (pageSize == 0) {
      limit = 10;
    }
    if (pageNum != null && pageNum > 0)
    	offset = (pageNum - 1) * limit;
    else 
    	offset = 0;
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
	  
      String startTime=pageInfo.getStartTime();
      String endTime=pageInfo.getEndTime();
	  if(startTime=="")
		  startTime=nowMonthOfFirstDay;
	  if(endTime=="")
		  endTime=nowDate;
	  String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	  if(searchValue != null) {
		  List<WalletTotalTradeRecordInfo> ethWalletOutTotalTradeRecordList=ethWalletTradeRecordMapper.selectAllOutTotalTradeRecordBySearchValue(startTime, endTime, offset, limit, searchValue);
		  PageDomain<WalletTotalTradeRecordInfo> walletOutTotalTradeRecordInfo = new PageDomain<>();
		  Integer count=ethWalletTradeRecordMapper.countByOutTotalTradeRecordBySearchValue(startTime, endTime, searchValue);
		  walletOutTotalTradeRecordInfo.setTotal(count);
		  walletOutTotalTradeRecordInfo.setAsc("desc");
		  walletOutTotalTradeRecordInfo.setOffset(offset);
		  walletOutTotalTradeRecordInfo.setPageNum(pageNum);
		  walletOutTotalTradeRecordInfo.setPageSize(pageSize);
		  walletOutTotalTradeRecordInfo.setRows(ethWalletOutTotalTradeRecordList);
			return new ResponseEntity.Builder<PageDomain<WalletTotalTradeRecordInfo>>()
			        .setData(walletOutTotalTradeRecordInfo)
			        .setErrorCode(ErrorCode.SUCCESS)
			        .build();
	  }else {
		  List<WalletTotalTradeRecordInfo> ethWalletOutTotalTradeRecordList=ethWalletTradeRecordMapper.selectAllOutTotalTradeRecord(startTime, endTime,offset, limit);
		  PageDomain<WalletTotalTradeRecordInfo> walletOutTotalTradeRecordInfo = new PageDomain<>();
		  Integer count=ethWalletTradeRecordMapper.countByOutTotalTradeRecord(startTime, endTime);
		  walletOutTotalTradeRecordInfo.setTotal(count);
		  walletOutTotalTradeRecordInfo.setAsc("desc");
		  walletOutTotalTradeRecordInfo.setOffset(offset);
		  walletOutTotalTradeRecordInfo.setPageNum(pageNum);
		  walletOutTotalTradeRecordInfo.setPageSize(pageSize);
		  walletOutTotalTradeRecordInfo.setRows(ethWalletOutTotalTradeRecordList);
			return new ResponseEntity.Builder<PageDomain<WalletTotalTradeRecordInfo>>()
			        .setData(walletOutTotalTradeRecordInfo)
			        .setErrorCode(ErrorCode.SUCCESS)
			        .build();
	  }
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
 
  @PostMapping(value="/backup")
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

  @PostMapping(value="/status")
  @ResponseBody
  public ResponseEntity<?> statusEthWallet(){
    EthWalletStatus ethWalletStatus = new EthWalletStatus();
    UserModel user = ShiroUtils.getUser();
	  if(user == null) {
		  return new ResponseEntity.Builder<EthWalletStatus>()
				  .setData(ethWalletStatus)
				  .setErrorCode(ErrorCode.USER_NOT_EXISTS).build();
	  }
	  ErrorCode errorCode = ethWalletService.statusEthWallet(user.getUuid(), ethWalletStatus);
	  return new ResponseEntity.Builder<EthWalletStatus>()
			  .setData(ethWalletStatus)
			  .setErrorCode(errorCode)
			  .build();
  }
   
   /**
   * 获取system在线钱包交易记录
   * @param pageInfo
   * @return
   */
  @PostMapping(value="/systemTransactionDetail")
  @ResponseBody()
  public ResponseEntity<?> systemTransactionDetail(@RequestBody PageDomain<Integer> pageInfo){
	  Integer pageNum = 1;
	  Integer pageSize = 10;
	  if(pageInfo != null) {
		  pageNum = (pageInfo.getPageNum() == null || pageInfo.getPageNum()<=0? pageNum:pageInfo.getPageNum());
		  pageSize = (pageInfo.getPageSize() == null || pageInfo.getPageSize()<=0? pageSize:pageInfo.getPageSize());
	  }

	  return new ResponseEntity.Builder<PageDomain<EthWalletDetail>>()
	            .setData(ethWalletDetailService.systemTransactionDetail(pageNum,pageSize))
	            .setErrorCode(ErrorCode.SUCCESS)
	            .build();
  }
  
}
