package com.cascv.oas.server.blockchain.controller;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.config.TransferQuota;
import com.cascv.oas.server.blockchain.mapper.EthWalletDetailMapper;
import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.model.EthWalletDetail;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;
import com.cascv.oas.server.blockchain.wrapper.EthWalletMultiTransfer;
import com.cascv.oas.server.blockchain.wrapper.EthWalletSummary;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransfer;
import com.cascv.oas.server.blockchain.wrapper.SelectContractSymbolName;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.utils.ShiroUtils;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
  
  @Autowired
  private EthWalletDetailMapper ethWalletDetailMapper;
  
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
	  if(ethWalletTransfer.getToUserAddress() == null) {
		  return new ResponseEntity.Builder<Integer>()
				  .setData(1)
				  .setErrorCode(ErrorCode.ADDRESS_CAN_NOT_BE_NULL)
				  .build();
	  }else if(ethWalletTransfer.getAmount() == null) {
		  return new ResponseEntity.Builder<Integer>()
				  .setData(1)
				  .setErrorCode(ErrorCode.VALUE_CAN_NOT_BE_NULL)
				  .build();
	  }else {
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

  @PostMapping(value="/transactionDetail")
  @ResponseBody()
  public ResponseEntity<?> transactionDetail(@RequestBody PageDomain<Integer> pageInfo){
    Integer count = ethWalletDetailMapper.selectCount();
    Integer pageNum = pageInfo.getPageNum();
    Integer pageSize = pageInfo.getPageSize();
    Integer limit = pageSize;
    Integer offset;
    if (pageNum > 0)
      offset = (pageNum - 1) * limit;
    else 
      offset = 0;
    
    EthWallet ethWallet = ethWalletService.getEthWalletByUserUuid(ShiroUtils.getUserUuid());
  
    List<EthWalletDetail> ethWalletDetailList = ethWalletDetailMapper.selectByPage(
        ethWallet.getAddress(), offset,limit);
    PageDomain<EthWalletDetail> pageEthWalletDetail= new PageDomain<>();
    pageEthWalletDetail.setTotal(count);
    pageEthWalletDetail.setAsc("desc");
    pageEthWalletDetail.setOffset(offset);
    pageEthWalletDetail.setPageNum(pageNum);
    pageEthWalletDetail.setPageSize(pageSize);
    pageEthWalletDetail.setRows(ethWalletDetailList);
    return new ResponseEntity.Builder<PageDomain<EthWalletDetail>>()
            .setData(pageEthWalletDetail)
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
