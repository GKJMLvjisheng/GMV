package com.cascv.oas.server.blockchain.controller;

import java.math.BigDecimal;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.service.UserWalletService;
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

  @PostMapping(value="/balance")
  @ResponseBody()
  public ResponseEntity<?> balance(){
    BigDecimal balance = BigDecimal.ZERO;
    UserWallet userWallet = userWalletService.find(ShiroUtils.getUserUuid());
    if (userWallet != null) {
      balance =  userWallet.getBalance();
    }
    return new ResponseEntity.Builder<BigDecimal>()
        .setData(balance)
        .setErrorCode(ErrorCode.NO_ONLINE_ACCOUNT).build();
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
