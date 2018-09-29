package com.cascv.oas.server.blockchain.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.PageIODomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletTradeRecordMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.blockchain.wrapper.TimeLimitInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletBalanceSummary;
import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTransfer;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping(value="/api/v1/userWallet")
public class UserWalletController {
  
  @Autowired
  private UserWalletService userWalletService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserWalletDetailMapper userWalletDetailMapper;

  @Autowired
  private UserWalletTradeRecordMapper userWalletTradeRecordMapper;
  
  @Autowired
  private ExchangeRateService exchangeRateService;

  @PostMapping(value="/inquireAddress")
  @ResponseBody()
  public ResponseEntity<?> inquireAddress(){
	Map<String, String> map = new HashMap<>();
    UserWallet userWallet = userWalletService.find(ShiroUtils.getUserUuid());
    ErrorCode errorCode= ErrorCode.NO_ONLINE_ACCOUNT;
    if (userWallet != null) {
    	map.put("address", ShiroUtils.getUser().getName());
    	errorCode= ErrorCode.SUCCESS;
    } 
    
    return new ResponseEntity.Builder<Map<String, String>>()
    	  .setData(map)
        .setErrorCode(errorCode).build();
  }
  
  
  @PostMapping(value="/balanceDetail")
  @ResponseBody()
  public ResponseEntity<?> balanceDetail(){
    UserWalletBalanceSummary userWalletBalanceSummary = new UserWalletBalanceSummary();
    UserWallet userWallet = userWalletService.find(ShiroUtils.getUserUuid());
    
    userWalletBalanceSummary.setOngoingBalance(0.0);
    userWalletBalanceSummary.setAvailableBalance(0.0);
    userWalletBalanceSummary.setAvailableBalanceValue(0.0);
    
    if (userWallet == null) {
      return new ResponseEntity.Builder<UserWalletBalanceSummary>()
          .setData(userWalletBalanceSummary)
          .setErrorCode(ErrorCode.NO_ONLINE_ACCOUNT).build();
    }
    BigDecimal balance = userWallet.getBalance();
      
    ReturnValue<BigDecimal> returnType = exchangeRateService.exchangeTo(
         balance, DateUtils.dateTimeNow(DateUtils.YYYY_MM), 
         CurrencyCode.CNY);
    if (returnType.getErrorCode() != ErrorCode.SUCCESS) {
        return new ResponseEntity.Builder<UserWalletBalanceSummary>()
            .setData(userWalletBalanceSummary)
            .setErrorCode(returnType.getErrorCode()).build();
    }
      
    BigDecimal value=returnType.getData();
    userWalletBalanceSummary.setAvailableBalance(balance.doubleValue());
    userWalletBalanceSummary.setAvailableBalanceValue(value.doubleValue());
    return new ResponseEntity.Builder<UserWalletBalanceSummary>()
      		.setData(userWalletBalanceSummary)
          .setErrorCode(ErrorCode.SUCCESS).build();
  }

  @PostMapping(value="/transactionDetail")
  @ResponseBody()
  public ResponseEntity<?> transactionDetail(@RequestBody PageIODomain<Integer> pageInfo){
    Calendar calendar = new GregorianCalendar();
    Date now = new Date();
    calendar.setTime(now);
    Integer pageNum = pageInfo.getPageNum();
    Integer pageSize;
    pageSize=pageInfo.getPageSize();
    Integer limit = pageSize;
    Integer offset;
    
    if (limit == null)
        limit = 10;
    if (pageNum != null && pageNum > 0)
      offset = (pageNum - 1) * limit;
    else 
      offset = 0;
    
    Integer inOrOut;
    log.info("inOrOut{}",pageInfo.getInOrOut());
    if(pageInfo.getInOrOut()!=null) {
    inOrOut=pageInfo.getInOrOut();
    List<UserWalletDetail> userWalletDetailList = userWalletDetailMapper.selectByInOrOut(ShiroUtils.getUserUuid(),offset,limit, inOrOut);
    Integer count =userWalletDetailList.size();
	PageDomain<UserWalletDetail> pageUserWalletDetail= new PageDomain<>();
	pageUserWalletDetail.setTotal(count);
	pageUserWalletDetail.setAsc("desc");
	pageUserWalletDetail.setOffset(offset);
	pageUserWalletDetail.setPageNum(pageNum);
	pageUserWalletDetail.setPageSize(pageSize);
	pageUserWalletDetail.setRows(userWalletDetailList);
	log.info("****转入/转出****");
	return new ResponseEntity.Builder<PageDomain<UserWalletDetail>>()
	    .setData(pageUserWalletDetail)
	    .setErrorCode(ErrorCode.SUCCESS)
	    .build();
    }
    else{
    List<UserWalletDetail> userWalletDetailList = userWalletDetailMapper.selectByPage(
    				ShiroUtils.getUserUuid(), offset,limit);
    Integer count = userWalletDetailMapper.selectCount(ShiroUtils.getUserUuid());
    PageDomain<UserWalletDetail> pageUserWalletDetail= new PageDomain<>();
    pageUserWalletDetail.setTotal(count);
    pageUserWalletDetail.setAsc("desc");
    pageUserWalletDetail.setOffset(offset);
    pageUserWalletDetail.setPageNum(pageNum);
    pageUserWalletDetail.setPageSize(pageSize);
    pageUserWalletDetail.setRows(userWalletDetailList);
    log.info("****全部****");
    return new ResponseEntity.Builder<PageDomain<UserWalletDetail>>()
            .setData(pageUserWalletDetail)
            .setErrorCode(ErrorCode.SUCCESS)
            .build();
   }
  }

  @PostMapping(value="/transfer")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> transferTo(@RequestBody UserWalletTransfer userWalletTransfer){
    UserModel fromUser=ShiroUtils.getUser();
    UserModel toUser =  userService.findUserByName(userWalletTransfer.getToUserName());
    //String rank = userWalletTransfer.getRank();
    if (fromUser == null || toUser == null) {
        return new ResponseEntity.Builder<Integer>()
        .setData(1)
        .setErrorCode(ErrorCode.USER_NOT_EXISTS)
        .build();
      }
    String fromUserName = fromUser.getName();
    String toUserName = toUser.getName();
    if(fromUserName.equals(toUserName)) {
    	return new ResponseEntity.Builder<Integer>()
    			.setData(1)
    			.setErrorCode(ErrorCode.CAN_NOT_TRANSFER_TO_SELF)
    	    .build();
    }
    ErrorCode errorCode = userWalletService.transfer(fromUser.getUuid(), toUser.getUuid(), userWalletTransfer.getValue(), userWalletTransfer.getRemark(), userWalletTransfer.getComment());
    return new ResponseEntity.Builder<Integer>()
        .setData(1)
        .setErrorCode(errorCode)
        .build();
  }
  
  /**
   * @author Ming Yang
   * @return 在线钱包交易明细接口
   */
  @PostMapping(value="/inqureUserWalletTradeRecord")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> inqureUserWalletTradeRecord(){
	  List<UserWalletTradeRecordInfo> userWalletTradeRecords=userWalletTradeRecordMapper.selectAllTradeRecord();
		return new ResponseEntity.Builder<List<UserWalletTradeRecordInfo>>()
		        .setData(userWalletTradeRecords)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
  }
  /**
   * @author Ming Yang
   * @return 在线钱包个人转入统计
   */
  @PostMapping(value="/inqureUserWalletInTotalTradeRecord")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> inqureUserWalletInTotalTradeRecord(@RequestBody TimeLimitInfo timeLimitInfo){
	  
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
	  
	  List<WalletTotalTradeRecordInfo> userWalletInTotalTradeRecords=userWalletTradeRecordMapper.selectAllInTotalTradeRecord(startTime, endTime);
		return new ResponseEntity.Builder<List<WalletTotalTradeRecordInfo>>()
		        .setData(userWalletInTotalTradeRecords)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
  }
  /**
   * @author Ming Yang
   * @return 在线钱包个人转出统计
   */
  @PostMapping(value="/inqureUserWalletOutTotalTradeRecord")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> inqureUserWalletOutTotalTradeRecord(@RequestBody TimeLimitInfo timeLimitInfo){
	  
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
	  
	  List<WalletTotalTradeRecordInfo> userWalletOutTotalTradeRecords=userWalletTradeRecordMapper.selectAllOutTotalTradeRecord(startTime, endTime);
		return new ResponseEntity.Builder<List<WalletTotalTradeRecordInfo>>()
		        .setData(userWalletOutTotalTradeRecords)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
  }
  
  /**
   * @author Ming Yang
   * @return 在线钱包余额统计
   */
  @PostMapping(value="/inqureUserWalletBalanceRecord")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> inqureUserWalletBalanceRecord(@RequestBody TimeLimitInfo timeLimitInfo){
	  
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
	  
	  List<WalletTotalTradeRecordInfo> userWalletBalanceRecords=userWalletTradeRecordMapper.selectAllUserBalanceRecord(startTime, endTime);
		return new ResponseEntity.Builder<List<WalletTotalTradeRecordInfo>>()
		        .setData(userWalletBalanceRecords)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
  }
}
