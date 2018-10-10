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

import com.alibaba.fastjson.JSONArray;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ReturnValue;
import com.cascv.oas.core.utils.CryptoUtils;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.config.CoinClient;
import com.cascv.oas.server.blockchain.config.TransferQuota;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.mapper.EthWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.EthWalletMapper;
import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.model.EthConfigModel;
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
  private static final String KEY_SALT = "anbInmSS76Mn9";
  
  private static SecureRandom secureRandom = new SecureRandom();
  
  @Autowired
  private EthWalletMapper ethWalletMapper;
  
  @Autowired
  private CoinClient coinClient;

  @Autowired
  private DigitalCoinService digitalCoinService;
  
  @Autowired
  private ExchangeRateService exchangeRateService;
  
  @Autowired
  private EthWalletDetailMapper ethWalletDetailMapper;
  
  @Autowired
  private KeyStoreService keyStoreService;
  
  public static String toMnemonicList(List<String> mnemonic) {
    JSONArray jsonArray = new JSONArray();
    for (String s : mnemonic) {
      jsonArray.add(s);
    }
    return jsonArray.toJSONString();
  }
  
  public static List<String> fromEncryptedMnemonicList(String encryptedMnemonic){
    if (encryptedMnemonic == null) {
      return null;
    }
    String mnemonic = CryptoUtils.decrypt(encryptedMnemonic, KEY_SALT);
    JSONArray jsonArray = JSONArray.parseArray(mnemonic);
    List<String> x = new ArrayList<>();
    for (Integer i = 0; i< jsonArray.size(); i++) {
      x.add(jsonArray.getString(i));
    }
    return x;
  }
  
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
      
      String mnemonicJson = toMnemonicList(ds.getMnemonicCode());
      String encryptedMnemonic = CryptoUtils.encrypt(mnemonicJson, KEY_SALT);
      log.info("encrypted mnemonic:{}", encryptedMnemonic);
      ethWallet.setMnemonicList(encryptedMnemonic);
      
      String publicKey = CryptoUtils.encrypt(keyPair.getPublicKey().toString(16), KEY_SALT);
      
      log.info("publickey : {}", publicKey);
      ethWallet.setPublicKey(publicKey);
      
      String privateKey = CryptoUtils.encrypt(keyPair.getPrivateKey().toString(16), KEY_SALT);
      log.info("privateKey : {}", privateKey);

      ethWallet.setPrivateKey(privateKey);
      
      ethWallet.setMnemonicPath(dkKey.getPathAsString());
      ethWallet.setAddress("0x" + walletFile.getAddress());
      
      String encryptedWallet = CryptoUtils.encrypt(jsonStr, KEY_SALT);
      keyStoreService.saveKey(userUuid, encryptedWallet);
      String datetime = DateUtils.dateTimeNow();
      ethWallet.setCrypto(1);
      ethWallet.setCreated(datetime);
      ethWallet.setUpdated(datetime);
      ethWalletMapper.deleteByUserUuid(userUuid);
      ethWalletMapper.insertSelective(ethWallet);
      return ethWallet;
    } catch (CipherException | JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public List<ContractSymbol> selectContractSymbol(String name) {
	  return digitalCoinService.selectContractSymbol(name);
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
      EthWallet ethWallet = getEthWalletByUserUuid(userUuid);
      log.info("userUuid {} ethWallet {}", userUuid, ethWallet);
      balance = coinClient.balanceOf(ethWallet.getAddress(), weiFactor);
      log.info("getBalance of {}", balance);
    } catch (Exception e) {
    	e.printStackTrace();
    }
    return balance;
  }
  
  public Double getEthBalance(String userUuid,BigDecimal weiFactor) {
    BigInteger balance = null;
    try {
      EthWallet ethWallet = getEthWalletByUserUuid(userUuid);
      balance = coinClient.ethBalance(ethWallet.getAddress());
      BigDecimal balanceDec = new BigDecimal(balance);
      balanceDec = balanceDec.divide(weiFactor);
      log.info("getEthBalance of {}", balanceDec);
      return balanceDec.doubleValue();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0.0;
  }
  
  
  public BigDecimal getValue(BigDecimal balance) {
    
    String time = DateUtils.dateTimeNow(DateUtils.YYYY_MM);
    ReturnValue<BigDecimal> returnValue = exchangeRateService.exchangeTo(
        balance, 
        time, 
        CurrencyCode.CNY);
    return returnValue.getData();
  }
  
  public UserCoin getUserCoin(String userUuid){
    EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
    if (ethWallet == null)
      return null;
    DigitalCoin digitalCoin = digitalCoinService.find(coinClient.getToken());
    if (digitalCoin == null)
      return null;
    UserCoin userCoin = new UserCoin();
    userCoin.setUserUuid(userUuid);
    

    userCoin.setContract(digitalCoin.getContract());
    userCoin.setWeiFactor(digitalCoin.getWeiFactor());
    userCoin.setAddress(ethWallet.getAddress());
    userCoin.setName(digitalCoin.getName());
    userCoin.setSymbol(digitalCoin.getSymbol());
    
    Double ethBalance = this.getEthBalance(userUuid, digitalCoin.getWeiFactor());
    BigDecimal balance=this.getBalance(userUuid, userCoin.getContract(),userCoin.getWeiFactor());
    userCoin.setEthBalance(ethBalance);
    userCoin.setBalance(balance);
    userCoin.setValue(this.getValue(balance));
    return userCoin;
  }

  public List<UserCoin> listCoin(String userUuid){
    List<UserCoin> userCoinList = new ArrayList<>();
    UserCoin userCoin = getUserCoin(userUuid);
    if (userCoin != null)
      userCoinList.add(userCoin);
    return userCoinList;
  }

  
  private void addDetail(String address, EthWalletDetailScope ethWalletDetailScope, 
    BigDecimal value, String txHash, String remark, String changeAddress) {
    EthWalletDetail ethWalletDetail = new EthWalletDetail();
    ethWalletDetail.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.USER_WALLET_DETAIL));
    ethWalletDetail.setAddress(address);
    ethWalletDetail.setTitle(ethWalletDetailScope.getTitle());   
    ethWalletDetail.setSubTitle(ethWalletDetailScope.getSubTitle()+changeAddress);
    log.info("changeAddress{}",changeAddress);
    ethWalletDetail.setInOrOut(ethWalletDetailScope.getInOrOut());
    ethWalletDetail.setValue(value);
    ethWalletDetail.setCreated(DateUtils.getTime());
    ethWalletDetail.setRemark(remark);
    ethWalletDetail.setTxHash(txHash);
    //ethWalletDetail.setChangeAddress(changeAddress);
    ethWalletDetailMapper.insertSelective(ethWalletDetail);
  }
  
  public ReturnValue<String> transfer(String userUuid, String toUserAddress, BigDecimal amount, BigInteger gasPrice, BigInteger gasLimit, String comment) {

    EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
    ReturnValue<String> returnValue = new ReturnValue<>();
    if (ethWallet == null) {
      returnValue.setErrorCode(ErrorCode.NO_ETH_WALLET);
      return returnValue;
    }
    UserCoin userCoin = this.getUserCoin(ethWallet.getUserUuid());
    if (userCoin.getBalance().compareTo(amount) < 0) {
      returnValue.setErrorCode(ErrorCode.BALANCE_NOT_ENOUGH);
      return returnValue;
    }
    BigDecimal amountDec = amount.multiply(userCoin.getWeiFactor());
    String key = ethWallet.getPrivateKey();
    if (ethWallet.getCrypto() != 0) {
      key = CryptoUtils.decrypt(key, KEY_SALT);
    }
    String txHash=coinClient.transfer(ethWallet.getAddress(), key, toUserAddress,  
    		amountDec.toBigInteger(), gasPrice, gasLimit);
    log.info("txhash {}", txHash);
    if (txHash != null) {
      addDetail(ethWallet.getAddress(), EthWalletDetailScope.TRANSFER_OUT,amount, txHash, comment, toUserAddress);
      addDetail(toUserAddress, EthWalletDetailScope.TRANSFER_IN, amount, txHash, comment, ethWallet.getAddress());
    }
    returnValue.setErrorCode(ErrorCode.SUCCESS);
    returnValue.setData(txHash);
    return returnValue;
  }
  /**
   * system向用户的交易钱包转账
   * @param userUuid 用户system的id
   * @param toUserAddress 用户交易钱包的地址
   * @param onlineWalletName 用户在线钱包地址
   * @param contract 
   * @param userCoin 
   * @param amount 
   * @param gasPrice 
   * @param gasLimit
   * @param comment
   * @return
   */
  public ReturnValue<String> systemTransfer(String userUuid, String toUserAddress,String onlineWalletName, UserCoin userCoin,BigDecimal amount, BigInteger gasPrice, BigInteger gasLimit, String comment) {
	    //system的交易钱包
	    EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
	    ReturnValue<String> returnValue = new ReturnValue<>();
	    if (ethWallet == null) {
	      returnValue.setErrorCode(ErrorCode.NO_ETH_WALLET);
	      return returnValue;
	    }
	    
	   /* String contract = coinClient.getToken();
	    UserCoin userCoin = this.getUserCoin(ethWallet.getUserUuid(), contract);*/
	    if (userCoin.getBalance().compareTo(amount) < 0) {
	      returnValue.setErrorCode(ErrorCode.BALANCE_NOT_ENOUGH);
	      return returnValue;
	    }
	    BigDecimal amountDec = amount.multiply(userCoin.getWeiFactor());
	    String key = ethWallet.getPrivateKey();
	    if (ethWallet.getCrypto() != 0) {
	      key = CryptoUtils.decrypt(key, KEY_SALT);
	    }
	    String txHash=coinClient.transfer(ethWallet.getAddress(), key, toUserAddress,amountDec.toBigInteger(), gasPrice, gasLimit);
	    log.info("txhash {}", txHash);
	    if (txHash != null) {
	      //addDetail(ethWallet.getAddress(), EthWalletDetailScope.COIN_TO_ETH,amount, txHash, comment, toUserAddress);
	      addDetail(onlineWalletName, EthWalletDetailScope.COIN_TO_ETH, amount, txHash, comment, toUserAddress);
	    }
	    returnValue.setErrorCode(ErrorCode.SUCCESS);
	    returnValue.setData(txHash);
	    return returnValue;
	  }
  
  public ReturnValue<String> multiTransfer(String userUuid, List<TransferQuota> quota,
		  BigInteger gasPrice, BigInteger gasLimit, String remark) {
    EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
    ReturnValue<String> returnValue = new ReturnValue<>();
    if (ethWallet == null) {
      returnValue.setErrorCode(ErrorCode.NO_ETH_WALLET);
      return returnValue;
    }
    UserCoin userCoin = this.getUserCoin(ethWallet.getUserUuid());
    List<BigInteger> amountIntList= new ArrayList<>();
    List<String> addressList = new ArrayList<>();
    BigDecimal total = BigDecimal.ZERO;
    for (TransferQuota q : quota) {
      total=total.add(q.getAmount());
      addressList.add(q.getToUserAddress());
      BigInteger amountInt=q.getAmount().multiply(userCoin.getWeiFactor()).toBigInteger();
      amountIntList.add(amountInt);      
    }
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
    String key = ethWallet.getPrivateKey();
    if (ethWallet.getCrypto() != 0) {
      key = CryptoUtils.decrypt(key, KEY_SALT);
    }
    String txHash=coinClient.multiTransfer(
    			ethWallet.getAddress(), key, 
    			addressList, amountIntList, gasPrice,gasLimit);
    log.info("txhash {}", txHash);
    if (txHash!=null) {
    	for (TransferQuota q: quota) {
	      addDetail(ethWallet.getAddress(), EthWalletDetailScope.TRANSFER_OUT, q.getAmount(), txHash, "", q.getToUserAddress());
    	}
	      for (TransferQuota q: quota) {
	        addDetail(q.getToUserAddress(), EthWalletDetailScope.TRANSFER_IN, q.getAmount(), txHash, "", ethWallet.getAddress()	);  
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
  
  public ErrorCode setPreferNetwork(String preferNetwork) {
	  Set<String> networkSet = this.listNetwork();
	  if (networkSet == null || preferNetwork == null || !networkSet.contains(preferNetwork))
		  return ErrorCode.INVALID_BLOCKCHAIN_NETWORK;
	  EthConfigModel ethConfigModel = digitalCoinService.getEthConfig(); 
	  if (ethConfigModel == null) {
	    ethConfigModel = new EthConfigModel();
	    ethConfigModel.setActiveNetwork(preferNetwork);
	    digitalCoinService.addEthConfig(ethConfigModel);
	  } else {
	    ethConfigModel.setActiveNetwork(preferNetwork);
	    digitalCoinService.updateEthConfig(ethConfigModel);
	  }
	  coinClient.setDefaultNet(preferNetwork);
	  return ErrorCode.SUCCESS;
  }
}



