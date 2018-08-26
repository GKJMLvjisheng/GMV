package com.cascv.oas.server.blockchain.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.blockchain.vo.UserWalletBalanceSummary;
import com.cascv.oas.server.blockchain.vo.UserWalletTransfer;
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

  @PostMapping(value="/balanceDetail")
  @ResponseBody()
  public ResponseEntity<?> balanceDetail(){
    UserWalletBalanceSummary userWalletBalanceSummary = new UserWalletBalanceSummary();
    UserWallet userWallet = userWalletService.find(ShiroUtils.getUserUuid());
    
    userWalletBalanceSummary.setOngoingBalance(BigDecimal.ZERO);
    if (userWallet != null) {
      userWalletBalanceSummary.setAvailableBalance(userWallet.getBalance());
      return new ResponseEntity.Builder<UserWalletBalanceSummary>()
      		.setData(userWalletBalanceSummary)
              .setErrorCode(ErrorCode.SUCCESS).build();
    } else {
      userWalletBalanceSummary.setAvailableBalance(BigDecimal.ZERO);
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
    List<UserWalletDetail> userWalletDetailList = new ArrayList<>();
    for (Integer i = 0; i < 3; i++) {
      UserWalletDetail userWalletDetail = new UserWalletDetail();
      userWalletDetail.setUuid(String.valueOf(i+1));
      userWalletDetail.setUserUuid(ShiroUtils.getUserUuid());
      userWalletDetail.setValue(BigDecimal.valueOf(i+100));
      userWalletDetail.setScope("手机");

      calendar.add(Calendar.DATE,1+i);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
      String str = formatter.format(calendar.getTime());
      userWalletDetail.setCreated(str);
      userWalletDetailList.add(userWalletDetail);
    }
    PageDomain<UserWalletDetail> pageUserWalletDetail= new PageDomain<>();
    pageUserWalletDetail.setTotal(3);
    pageUserWalletDetail.setAsc("asc");
    pageUserWalletDetail.setOffset(0);
    pageUserWalletDetail.setPageNum(1);
    pageUserWalletDetail.setPageSize(3);
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
