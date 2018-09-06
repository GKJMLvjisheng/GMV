package com.cascv.oas.server.blockchain.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.config.ExchangeParam;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.blockchain.wrapper.UserWalletBalanceSummary;
import com.cascv.oas.server.blockchain.wrapper.UserWalletTransfer;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.UserService;
import com.cascv.oas.server.utils.ShiroUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
  private ExchangeParam exchangeParam;

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
    
    userWalletBalanceSummary.setOngoingBalance(BigDecimal.ZERO);
    if (userWallet != null) {
      BigDecimal balance = userWallet.getBalance();
      
      BigDecimal factor = BigDecimal.valueOf(exchangeParam.getTokenRmbRate());
      BigDecimal value=balance.multiply(factor);
      userWalletBalanceSummary.setAvailableBalance(balance);
      userWalletBalanceSummary.setAvailableBalanceValue(value);
      return new ResponseEntity.Builder<UserWalletBalanceSummary>()
      		.setData(userWalletBalanceSummary)
              .setErrorCode(ErrorCode.SUCCESS).build();
    } else {
      userWalletBalanceSummary.setAvailableBalance(BigDecimal.ZERO);
      userWalletBalanceSummary.setAvailableBalanceValue(BigDecimal.ZERO);
    	return new ResponseEntity.Builder<UserWalletBalanceSummary>()
    		.setData(userWalletBalanceSummary)
        .setErrorCode(ErrorCode.NO_ONLINE_ACCOUNT).build();
    }
  }

  @PostMapping(value="/transactionDetail")
  @ResponseBody()
  public ResponseEntity<?> transactionDetail(@RequestBody PageDomain<Integer> pageInfo){
    Calendar calendar = new GregorianCalendar();
    Date now = new Date();
    calendar.setTime(now);
    
    
    Integer pageNum = pageInfo.getPageNum();
    Integer pageSize = pageInfo.getPageSize();
    Integer limit = pageSize;
    Integer offset;
    if (pageNum > 0)
    	offset = (pageNum - 1) * limit;
    else 
    	offset = 0;

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
    return new ResponseEntity.Builder<PageDomain<UserWalletDetail>>()
            .setData(pageUserWalletDetail)
            .setErrorCode(ErrorCode.SUCCESS)
            .build();
  }

  @PostMapping(value="/transfer")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> transferTo(@RequestBody UserWalletTransfer userWalletTransfer){
    UserModel fromUser=ShiroUtils.getUser();
    UserModel toUser =  userService.findUserByName(userWalletTransfer.getToUserName());
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
    ErrorCode errorCode = userWalletService.transfer(fromUser.getUuid(), toUser.getUuid(), userWalletTransfer.getValue());
    return new ResponseEntity.Builder<Integer>()
        .setData(1)
        .setErrorCode(errorCode)
        .build();
  }
}
