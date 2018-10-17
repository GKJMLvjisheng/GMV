package com.cascv.oas.server.blockchain.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.PageIODomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletTradeRecordMapper;
import com.cascv.oas.server.blockchain.model.OasDetail;
import com.cascv.oas.server.blockchain.model.OasDetailResp;
import com.cascv.oas.server.blockchain.model.OasReq;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.blockchain.service.UserWalletDetailService;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.blockchain.wrapper.TimeLimitInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletBalanceSummary;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTradeRecordInfo;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTransfer;
import com.cascv.oas.server.blockchain.wrapper.WalletTotalTradeRecordInfo;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.shiro.BaseShiroController;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping(value="/api/v1/userWallet")
public class UserWalletController extends BaseShiroController {
  
  @Autowired
  private UserWalletService userWalletService;

  @Autowired
  private UserService userService;

  @Autowired
  private UserWalletDetailMapper userWalletDetailMapper;
  @Autowired 
  private TimeZoneService timeZoneService;
  @Autowired
  private UserWalletTradeRecordMapper userWalletTradeRecordMapper;
  
  @Autowired
  private ExchangeRateService exchangeRateService;
  @Autowired
  private UserWalletDetailService userWalletDetailService;
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
    userWalletBalanceSummary.setOngoingBalance(userWallet.getUnconfirmedBalance().doubleValue());
    return new ResponseEntity.Builder<UserWalletBalanceSummary>()
      		.setData(userWalletBalanceSummary)
          .setErrorCode(ErrorCode.SUCCESS).build();
  }

  @PostMapping(value="/transactionDetail")
  @ResponseBody()
  public ResponseEntity<?> transactionDetail(@RequestBody PageIODomain<Integer> pageInfo){
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
    List<UserWalletDetail> userWalletDetailList = userWalletDetailService.selectByInOrOut(ShiroUtils.getUserUuid(),offset,limit, inOrOut);
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
    List<UserWalletDetail> userWalletDetailList = userWalletDetailService.selectByPage(
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
  @RequiresPermissions("在线钱包-转账")
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
   * 提币请求申请
   * @param oasDetail
   * @return
   */
  
  @PostMapping(value="/withdraw")
  @RequiresPermissions("提币")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> withdraw(@RequestBody OasDetail oasDetail){
	  UserModel user = ShiroUtils.getUser();
	  if(user == null) {
		  return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(ErrorCode.USER_NOT_EXISTS).build();
	  }
	  if(oasDetail.getValue()== null || oasDetail.getValue().compareTo(BigDecimal.ZERO) ==0) {
		  return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(ErrorCode.VALUE_CAN_NOT_BE_NULL).build();
	  }
	  oasDetail.setUserUuid(user.getUuid());
	  return new ResponseEntity.Builder<Integer>().setData(1).setErrorCode(userWalletService.withdraw(oasDetail)).build();
  }
  
  /**
   * 获取提币记录
   * @return
   */
  @PostMapping(value="/getWithdrawList")
  @ResponseBody
  @RequiresRoles("admin")
  public ResponseEntity<?> getWithdrawList(){  
	  return new ResponseEntity.Builder<List<OasDetailResp>>()
		        .setData(userWalletService.getWithdrawList())
		        .setErrorCode(ErrorCode.SUCCESS).build();
  }
  /**
   * 管理员操作提币请求
   * @param uuid
   * @param result，1：同意，2：拒绝
   * @return
   */
  @PostMapping(value="/setWithdrawResult")
  @ResponseBody
  @RequiresRoles("admin")
  @Transactional
  public ResponseEntity<?> setWithdrawResult(@RequestBody OasDetailResp req){ 
	  String id = req.getUuid();
	  Integer result = req.getStatus();
	  if(id == null || result == null || (result != 1 && result != 2)) {
		  return new ResponseEntity.Builder<Integer>()
			        .setData(1)
			        .setErrorCode(ErrorCode.INPUT_ILLEGAL).build();
	  }
	  return new ResponseEntity.Builder<Integer>()
		        .setData(1)
		        .setErrorCode(userWalletService.setWithdrawResult(id,result)).build();
  }

  /**
   * 获取提币手续费
   * @return
   */
  @GetMapping(value="/getOasExtra")
  @ResponseBody
  public ResponseEntity<?> getOasExtra() {
	  return new ResponseEntity.Builder<String>()
		        .setData(userWalletService.getOasExtra())
		        .setErrorCode(ErrorCode.SUCCESS).build();
  }
  /**
   * 设置提币手续费
   * @param value
   * @return
   */
  @PostMapping(value="/updateOasExtra")
  @ResponseBody
  @RequiresRoles("admin")
  public ResponseEntity<?> updateOasExtra(@RequestBody OasReq oasDetail){
	  String value = oasDetail.getValue().toString();
	  if(value == null || !NumberUtils.isNumber(value)) {
		  return new ResponseEntity.Builder<Integer>()
			        .setData(1)
			        .setErrorCode(ErrorCode.INPUT_ILLEGAL).build();
	  }
	  return new ResponseEntity.Builder<Integer>()
		        .setData(1)
		        .setErrorCode(userWalletService.updateOasExtra(value)).build();
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
	  for (UserWalletTradeRecordInfo userWalletTradeRecordInfo : userWalletTradeRecords) {
			String srcFormater="yyyy-MM-dd HH:mm:ss";
			String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String created=DateUtils.string2Timezone(srcFormater, userWalletTradeRecordInfo.getCreated(), dstFormater, dstTimeZoneId);
			userWalletTradeRecordInfo.setCreated(created);
			log.info("newCreated={}",created);
		  }
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
  public ResponseEntity<?> inqureUserWalletBalanceRecord(){
	  
	  List<WalletTotalTradeRecordInfo> userWalletBalanceRecords=userWalletTradeRecordMapper.selectAllUserBalanceRecord();
		return new ResponseEntity.Builder<List<WalletTotalTradeRecordInfo>>()
		        .setData(userWalletBalanceRecords)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
  }
  /**
   * @author Ming Yang
   * @return 个人单日转账总额
   */
	@PostMapping(value="/inqureDailyTotalAmount")
	@ResponseBody
	public ResponseEntity<?> inqureDailyTotalAmount(){
		String created=DateUtils.getTime();
		log.info("created={}",created);
		String [] arr = created.split("\\s+");
		created=arr[0];
		String userUuid=ShiroUtils.getUserUuid();
		BigDecimal dailyTotalAmount=userWalletDetailMapper.selectUserDailyTotalAmount(userUuid, created);
		if(dailyTotalAmount==null)
			dailyTotalAmount=new BigDecimal(0);
		log.info("当日转账总金额：{}",dailyTotalAmount);
		return new ResponseEntity.Builder<BigDecimal>()
		        .setData(dailyTotalAmount)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
	}
}
