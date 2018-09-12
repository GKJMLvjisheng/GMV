package com.cascv.oas.server.blockchain.controller;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.server.blockchain.mapper.EthWalletDetailMapper;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.model.EthWalletDetail;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;
import com.cascv.oas.server.blockchain.wrapper.EthWalletMultiTransfer;
import com.cascv.oas.server.blockchain.wrapper.EthWalletMultiTransferResp;
import com.cascv.oas.server.blockchain.wrapper.EthWalletSummary;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransfer;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransferResp;
import com.cascv.oas.server.blockchain.wrapper.PreferNetworkReq;
import com.cascv.oas.server.blockchain.wrapper.SelectContractSymbolName;
import com.cascv.oas.server.utils.ShiroUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.utils.Convert;

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
	  System.out.println(ethWalletTransfer.getContract());
	  System.out.println(ethWalletTransfer.getToUserAddress());
	  System.out.println(ethWalletTransfer.getAmount());
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
	      
		  BigInteger gasLimit =  ethWalletTransfer.getGasLimit();
	      if (gasLimit == null)
	    	  gasLimit = BigInteger.valueOf(60000);
	      ReturnValue<String> returnValue=ethWalletService.transfer(
	        ShiroUtils.getUserUuid(), 
	        ethWalletTransfer.getContract(),
	        ethWalletTransfer.getToUserAddress(),
	        ethWalletTransfer.getAmount(),gasPrice,gasLimit);	  
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
  public ResponseEntity<?> multiTtransfer(@RequestBody EthWalletMultiTransfer ethWalletMultiTransfer){
	  
		  BigInteger gasPrice = ethWalletMultiTransfer.getGasPrice();
		  System.out.println("gasPrice"+gasPrice);
		  if (gasPrice == null)
			  System.out.println("gasPrice1"+gasPrice);
			gasPrice = Convert.toWei(BigDecimal.valueOf(6), Convert.Unit.GWEI).toBigInteger();
		  BigInteger gasLimit = ethWalletMultiTransfer.getGasLimit();
		  System.out.println("gasLimit"+gasLimit);
		  if (gasLimit == null)
			  gasLimit = BigInteger.valueOf(600000);
		  ReturnValue<String> returnValue=ethWalletService.multiTransfer(
	        ShiroUtils.getUserUuid(), 
	        ethWalletMultiTransfer.getContract(),
          ethWalletMultiTransfer.getQuota(), gasPrice, gasLimit);
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
    
    return new ResponseEntity.Builder<List<UserCoin>>()
            .setData(userCoinList)
            .setErrorCode(ErrorCode.SUCCESS)
            .build();
  }

  @PostMapping(value="/transactionDetail")
  @ResponseBody()
  public ResponseEntity<?> transactionDetail(@RequestBody PageDomain<Integer> pageInfo){
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
  
    List<EthWalletDetail> ethWalletDetailList = ethWalletDetailMapper.selectByPage(
        ethWallet.getAddress(), offset,limit);
    Integer count = ethWalletDetailMapper.selectCount(ethWallet.getAddress());
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
  
  @PostMapping(value="/listNetwork")
  @ResponseBody
  public ResponseEntity<?> listNetwork(){
	Set<String> networks = ethWalletService.listNetwork();
	if (networks == null) {
		return new ResponseEntity.Builder<Set<String>>()
		        .setData(networks)
		        .setErrorCode(ErrorCode.NO_BLOCKCHAIN_NETWORK)
		        .build();
	} else {
		return new ResponseEntity.Builder<Set<String>>()
		        .setData(networks)
		        .setErrorCode(ErrorCode.SUCCESS)
		        .build();
	}
  }
  
  @PostMapping(value="/setPreferNetwork")
  @ResponseBody
  @Transactional
  public ResponseEntity<?> setPreferNetwork(@RequestBody PreferNetworkReq req){
    ErrorCode errorCode=ethWalletService.setPreferNetwork(ShiroUtils.getUserUuid(), req.getPreferNetwork());
    return new ResponseEntity.Builder<Integer>()
        .setData(1)
        .setErrorCode(errorCode)
        .build();
  }
  
}
