package com.cascv.oas.server.blockchain.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Wallet;
import org.web3j.crypto.WalletFile;
import org.web3j.protocol.ObjectMapperFactory;
import org.web3j.utils.Numeric;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.config.CoinClient;
import com.cascv.oas.server.blockchain.config.TransferQuota;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.mapper.EthWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.EthWalletMapper;
import com.cascv.oas.server.blockchain.mapper.UserCoinMapper;
import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.model.EthWalletDetail;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;
import com.cascv.oas.server.common.EthWalletDetailScope;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EthWalletService {
  public static final String ETH_TYPE = "m/44'/60'/0'/0/0"; 
  
  private static SecureRandom secureRandom = new SecureRandom();
  
  @Autowired
  private EthWalletMapper ethWalletMapper;
  
  @Autowired
  private CoinClient coinClient;

  @Autowired
  private DigitalCoinService digitalCoinService;
  
  @Autowired
  private UserCoinMapper userCoinMapper;
  
  @Autowired
  private ExchangeRateService exchangeRateService;
  
  @Autowired
  private EthWalletDetailMapper ethWalletDetailMapper;
  
  @Autowired
  private KeyStoreService keyStoreService;
  
  public boolean checkMnemonic(String password, List <String> mnemonic) {
    
    try {
      byte[] mnemonicSeedBytes = MnemonicCode.INSTANCE.toEntropy(mnemonic);
      ECKeyPair mnemonicKeyPair = ECKeyPair.create(mnemonicSeedBytes);
      WalletFile walletFile = Wallet.createLight(password, mnemonicKeyPair);
      ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
      String walletFileStr = objectMapper.writeValueAsString(walletFile);
      WalletFile checkWalletFile = objectMapper.readValue(walletFileStr, WalletFile.class);
      ECKeyPair ecKeyPair = Wallet.decrypt(password, checkWalletFile);
      byte[] checkMnemonicSeedBytes = Numeric.hexStringToByteArray(ecKeyPair.getPrivateKey().toString(16));
      MnemonicCode.INSTANCE.toMnemonic(checkMnemonicSeedBytes);
    } catch (MnemonicException.MnemonicLengthException 
          | MnemonicException.MnemonicWordException 
          | MnemonicException.MnemonicChecksumException 
          | CipherException 
          | IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
  
  public Integer destroy(String userUuid){
    userCoinMapper.deleteAll(userUuid);
    ethWalletMapper.deleteByUserUuid(userUuid);
    keyStoreService.destroyKey(userUuid);
    return 0;
  }

  public EthWallet create(String userUuid, String password){
    String passphrase = "";
    long creationTimeSeconds = System.currentTimeMillis() / 1000;
    DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase, creationTimeSeconds);
    
    if (!checkMnemonic(password, ds.getMnemonicCode())) {
      log.error("check Mnemonic failure\n");
      return null;
    }
    byte[] seedBytes = ds.getSeedBytes();
    if (seedBytes == null) {
      return null;
    }
    DeterministicKey dkKey = HDKeyDerivation.createMasterPrivateKey(seedBytes);
    String[] pathArray = ETH_TYPE.split("/");
    for (int i = 1; i < pathArray.length; i++) {
      ChildNumber childNumber;
      if (pathArray[i].endsWith("'")) {
        int number = Integer.parseInt(pathArray[i].substring(0, pathArray[i].length() - 1));
        childNumber = new ChildNumber(number, true);
      } else {
        int number = Integer.parseInt(pathArray[i]);
        childNumber = new ChildNumber(number, false);
      }
      dkKey = HDKeyDerivation.deriveChildKey(dkKey, childNumber);
    }
    ECKeyPair keyPair = ECKeyPair.create(dkKey.getPrivKeyBytes());
    
    try {
      WalletFile walletFile = Wallet.createLight(password, keyPair);
      ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
      String jsonStr = objectMapper.writeValueAsString(walletFile);
      EthWallet ethWallet =  new EthWallet();
      ethWallet.setUuid(UuidUtils.getPrefixUUID("EW"));
      ethWallet.setUserUuid(userUuid);
      ethWallet.setMnemonicList(EthWallet.toMnemonicList(ds.getMnemonicCode()));
      ethWallet.setPublicKey(keyPair.getPublicKey().toString(16));
      ethWallet.setPrivateKey(keyPair.getPrivateKey().toString(16));
      ethWallet.setMnemonicPath(dkKey.getPathAsString());
      ethWallet.setAddress("0x" + walletFile.getAddress());
      keyStoreService.saveKey(userUuid, jsonStr);
      String datetime = DateUtils.dateTimeNow();
      ethWallet.setCreated(datetime);
      ethWallet.setUpdated(datetime);
      ethWalletMapper.deleteByUserUuid(userUuid);
      ethWalletMapper.insertSelective(ethWallet);
      import_token(userUuid, ethWallet.getAddress(),coinClient.getToken());
      return ethWallet;
    } catch (CipherException | JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public List<ContractSymbol> selectContractSymbol(String name) {
	  return digitalCoinService.selectContractSymbol(name);
  }

  public void import_token(String userUuid, String address, String contract){
    DigitalCoin digitalCoin = digitalCoinService.find(contract);
    UserCoin userCoin = new UserCoin();
    userCoin.setAddress(address);
    userCoin.setContract(contract);
    userCoin.setUserUuid(userUuid);
    userCoin.setName(digitalCoin.getName());
    userCoin.setSymbol(digitalCoin.getSymbol());
    
    BigDecimal weiFactor = digitalCoin.getWeiFactor();
    userCoin.setWeiFactor(weiFactor);
    BigDecimal balance = this.getBalance(address, contract, weiFactor);
    userCoin.setBalance(balance);
    userCoinMapper.insertSelective(userCoin);
  }
  
  public List<DigitalCoin> listDigitalCoins(){
    return digitalCoinService.listDigitalCoins();	  
  }

  public EthWallet getEthWalletByUserUuid(String userUuid)  {
    return ethWalletMapper.selectByUserUuid(userUuid);
  }

  public BigDecimal getBalance(String userUuid, String contract, BigDecimal weiFactor) {
    BigDecimal balance = BigDecimal.ZERO;
    try {
      EthWallet ethWallet = ethWalletMapper.selectByUserUuid(userUuid);
      String net = ethWallet.getPreferNetwork();
      if (net == null)
    	  net = coinClient.getDefaultNet();
      balance = coinClient.balanceOf(net, ethWallet.getAddress(), contract, weiFactor);
      log.info("getBalance of {}", balance);
    } catch (Exception e) {

    }
    return balance;
  }
  
  public BigDecimal getValue(BigDecimal balance) {
    
    String time = DateUtils.dateTimeNow(DateUtils.YYYY_MM);
    ReturnValue<BigDecimal> returnValue = exchangeRateService.exchangeTo(
        balance, 
        time, 
        CurrencyCode.CNY);
    return returnValue.getData();
  }
  
  public UserCoin getUserCoin(String userUuid, String contract){
    UserCoin userCoin = userCoinMapper.selectOne(userUuid, contract);
    if (userCoin == null)
      return null;
    BigDecimal balance=this.getBalance(userUuid, userCoin.getContract(),userCoin.getWeiFactor());
    userCoin.setBalance(balance);
    userCoin.setValue(this.getValue(balance));
    return userCoin;
  }
  
  public UserCoin getTokenCoin(String userUuid){
    return this.getUserCoin(userUuid, coinClient.getToken());
  }


  public List<UserCoin> listCoin(String userUuid){
    List<UserCoin> userCoinList = userCoinMapper.selectAll(userUuid);
    for (UserCoin coin:userCoinList) {
      BigDecimal balance =this.getBalance(userUuid, coin.getContract(),coin.getWeiFactor()); 
      coin.setBalance(balance);
      coin.setValue(this.getValue(balance));
    }
    return userCoinList;
  }

  
  private void addDetail(String address, EthWalletDetailScope userWalletDetailScope, BigDecimal value, String txHash, String comment) {
    EthWalletDetail ethWalletDetail = new EthWalletDetail();
    ethWalletDetail.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.USER_WALLET_DETAIL));
    ethWalletDetail.setAddress(address);
    ethWalletDetail.setTitle(userWalletDetailScope.getTitle());
    ethWalletDetail.setSubTitle(userWalletDetailScope.getSubTitle());
    ethWalletDetail.setInOrOut(userWalletDetailScope.getInOrOut());
    ethWalletDetail.setValue(value);
    ethWalletDetail.setCreated(DateUtils.getTime());
    ethWalletDetail.setComment(comment);
    ethWalletDetail.setTxHash(txHash);
    ethWalletDetailMapper.insertSelective(ethWalletDetail);
  }
  
  public ReturnValue<String> transfer(String userUuid, String contract, String toAddress, BigDecimal amount, BigInteger gasPrice, BigInteger gasLimit) {

    EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
    ReturnValue<String> returnValue = new ReturnValue<>();
    if (ethWallet == null) {
      returnValue.setErrorCode(ErrorCode.NO_ETH_WALLET);
      return returnValue;
    }
    if (contract == null)
      contract = coinClient.getToken();
    UserCoin userCoin = this.getUserCoin(ethWallet.getUserUuid(), contract);
    if (userCoin.getBalance().compareTo(amount) < 0) {
      returnValue.setErrorCode(ErrorCode.BALANCE_NOT_ENOUGH);
      return returnValue;
    }
    BigDecimal amountDec = amount.multiply(userCoin.getWeiFactor());
    String net = ethWallet.getPreferNetwork();
    if (net == null)
  	  net = coinClient.getDefaultNet();
    String txHash=coinClient.transfer(net, ethWallet.getAddress(), ethWallet.getPrivateKey(), toAddress, contract, 
    		amountDec.toBigInteger(), gasPrice, gasLimit);
    log.info("txhash {}", txHash);
    if (txHash != null) {
      addDetail(ethWallet.getAddress(), EthWalletDetailScope.TRANSFER_OUT,amount, txHash, "");
      addDetail(toAddress, EthWalletDetailScope.TRANSFER_IN, amount, txHash, "");
    }
    returnValue.setErrorCode(ErrorCode.SUCCESS);
    returnValue.setData(txHash);
    return returnValue;
  }
  
  public ReturnValue<String> multiTransfer(String userUuid, String contract, List<TransferQuota> quota,
		  BigInteger gasPrice, BigInteger gasLimit) {
    EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
    ReturnValue<String> returnValue = new ReturnValue<>();
    if (ethWallet == null) {
      returnValue.setErrorCode(ErrorCode.NO_ETH_WALLET);
      return returnValue;
    }
    if (contract == null)
      contract = coinClient.getToken();
    UserCoin userCoin = this.getUserCoin(ethWallet.getUserUuid(), contract);
    
    List<BigInteger> amountIntList= new ArrayList<>();
    List<String> addressList = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;
    for (TransferQuota q : quota) {
      total=total.add(q.getAmount());
      addressList.add(q.getToUserAddress());
      BigInteger amountInt=q.getAmount().multiply(userCoin.getWeiFactor()).toBigInteger();
      amountIntList.add(amountInt);      
    }
    String net = ethWallet.getPreferNetwork();
    if (net == null)
  	  net = coinClient.getDefaultNet();
    String txHash=coinClient.multiTransfer(
    			net, ethWallet.getAddress(), ethWallet.getPrivateKey(), 
    			addressList, contract, amountIntList, gasPrice,gasLimit);
    log.info("txhash {}", txHash);
    for (int i = 0; i < quota.size(); i++) {
    	if (addressList.get(i).isEmpty() || addressList.get(i).equals("0")) {
    		returnValue.setErrorCode(ErrorCode.WRONG_ADDRESS);
    	    return returnValue;
    	}
    }
    for (int i = 0; i < quota.size(); i++) {
    	if (amountIntList.get(i).intValue() == 0 || amountIntList.get(i).equals(null)) {
    		returnValue.setErrorCode(ErrorCode.WRONG_AMOUNT);
    		return returnValue;
    	}
    }
    if (txHash!=null) {
	      addDetail(ethWallet.getAddress(), EthWalletDetailScope.TRANSFER_OUT, total, txHash, "");
	      for (TransferQuota q: quota) {
	        addDetail(q.getToUserAddress(), EthWalletDetailScope.TRANSFER_IN, q.getAmount(), txHash, "");  
	      }
	      returnValue.setErrorCode(ErrorCode.SUCCESS);
	      returnValue.setData(txHash);
	      return returnValue;
	} else {
	      returnValue.setErrorCode(ErrorCode.MULTIPLE_TRANSFER_FAILURE);
	      return returnValue;
	    }

  }
  
  public Set<String> listNetwork() {
	  return coinClient.listNetwork();
  }
  
  public ErrorCode setPreferNetwork(String userUuid, String preferNetwork) {
	  Set<String> networkSet = this.listNetwork();
	  if (preferNetwork == null)
		  preferNetwork = "ropstrn";
	  if (networkSet == null || !networkSet.contains(preferNetwork))
		  return ErrorCode.INVALID_BLOCKCHAIN_NETWORK;
	  EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
	  if (ethWallet == null)
		  return ErrorCode.NO_ETH_WALLET;
	  ethWallet.setPreferNetwork(preferNetwork);
	  ethWalletMapper.update(ethWallet);
	  return ErrorCode.SUCCESS;
  }
}



