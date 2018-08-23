package com.cascv.oas.server.blockchain.controller;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.model.EthHdWallet;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.utils.ShiroUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/ethHdWallet")
public class EthWalletController {
  
  @Autowired
  private EthWalletService ethWalletService;

  @PostMapping(value="/testAPi")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> testAPi(){
    UserModel userModel=ShiroUtils.getUser();
    EthHdWallet ethHdWallet = ethWalletService.getEthWalletByUserUuid(userModel.getUuid());
    ethWalletService.testWeb(ethHdWallet);
    return new ResponseEntity.Builder<Integer>()
        .setData(1)
        .setErrorCode(ErrorCode.SUCCESS)
        .build();
  }
}
