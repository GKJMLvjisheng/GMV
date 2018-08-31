package com.cascv.oas.server.blockchain.controller;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;
import com.cascv.oas.server.blockchain.wrapper.EthWalletMultiTransfer;
import com.cascv.oas.server.blockchain.wrapper.EthWalletSummary;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransfer;
import com.cascv.oas.server.blockchain.wrapper.SelectContractSymbolName;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.utils.ShiroUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/api/v1/ethWallet")
public class EthWalletController {
  
  @Autowired
  private EthWalletService ethWalletService;
  
  @PostMapping(value="/selectContractSymbol")
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
	  
  }

  @PostMapping(value="/transfer")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> transfer(@RequestBody EthWalletTransfer ethWalletTransfer){
    ErrorCode errorCode=ethWalletService.transfer(
        ShiroUtils.getUserUuid(), 
        ethWalletTransfer.getContract(),
        ethWalletTransfer.getToUserAddress(),
        ethWalletTransfer.getAmount());

    return new ResponseEntity.Builder<Integer>()
        .setData(1)
        .setErrorCode(errorCode)
        .build();
  }

  @PostMapping(value="/multiTtransfer")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> multiTtransfer(@RequestBody EthWalletMultiTransfer ethWalletMultiTransfer){
    ErrorCode errorCode=ethWalletService.multiTransfer(
        ShiroUtils.getUserUuid(), 
        ethWalletMultiTransfer.getContract(),
        ethWalletMultiTransfer.getQuota());

    return new ResponseEntity.Builder<Integer>()
        .setData(1)
        .setErrorCode(errorCode)
        .build();
  }
  
  
  @PostMapping(value="/listCoin")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> listCoin(){
    String userUuid = ShiroUtils.getUserUuid();
    List<UserCoin> userCoinList = ethWalletService.listCoin(userUuid);
    
    return new ResponseEntity.Builder<List<UserCoin>>()
            .setData(userCoinList)
            .setErrorCode(ErrorCode.SUCCESS)
            .build();
  }

  @PostMapping(value="/summary")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> summary(){
    ErrorCode errorCode=ErrorCode.SUCCESS;
    UserCoin tokenCoin = ethWalletService.getTokenCoin(ShiroUtils.getUserUuid());
    
    EthWalletSummary ethWalletSummary = new EthWalletSummary();
    ethWalletSummary.setTotalTransaction(BigDecimal.ZERO);

    if (tokenCoin != null) {
      ethWalletSummary.setTotalBalance(tokenCoin.getBalance());
      ethWalletSummary.setTotalValue(tokenCoin.getValue());
    } else {
      ethWalletSummary.setTotalBalance(BigDecimal.ZERO);
      ethWalletSummary.setTotalValue(BigDecimal.ZERO);
    }
    return new ResponseEntity.Builder<EthWalletSummary>()
        .setData(ethWalletSummary)
        .setErrorCode(errorCode)
        .build();
  }
}
