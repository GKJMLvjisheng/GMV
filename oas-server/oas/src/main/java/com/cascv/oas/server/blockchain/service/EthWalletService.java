package com.cascv.oas.server.blockchain.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.wallet.DeterministicSeed;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
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
import com.cascv.oas.core.utils.HttpUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.config.CoinClient;
import com.cascv.oas.server.blockchain.config.TransferQuota;
import com.cascv.oas.server.blockchain.constant.OasEventEnum;
import com.cascv.oas.server.blockchain.job.EtherRedeemJob;
import com.cascv.oas.server.blockchain.mapper.EthWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.EthWalletMapper;
import com.cascv.oas.server.blockchain.mapper.OasDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.model.EthConfigModel;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.model.EthWalletDetail;
import com.cascv.oas.server.blockchain.model.OasDetail;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.wrapper.BackupEthWallet;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;
import com.cascv.oas.server.blockchain.wrapper.EthWalletTransfer;
import com.cascv.oas.server.common.EthWalletDetailScope;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.model.ExchangeRateModel;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.scheduler.service.SchedulerService;
import com.cascv.oas.server.user.model.UserModel;
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
  
  @Autowired
  private OasDetailMapper oasDetailMapper;
  
  @Autowired
  private UserWalletMapper userWalletMapper;
  
  @Autowired
  private SchedulerService schedulerService;
 
  @Autowired
  private UserWalletDetailMapper userWalletDetailMapper;


  public synchronized void updateJob() {
    log.info("update job ...");
    List<EthWalletDetail> ethWalletDetailList = ethWalletDetailMapper.selectEthTransactionJob(coinClient.getNetName(), 30); 
    if (ethWalletDetailList != null && ethWalletDetailList.size() > 0) {
      for (EthWalletDetail ethWalletDetail:ethWalletDetailList) {
        Integer status = coinClient.getTransactionStatus(ethWalletDetail.getTxHash());
        log.info("txHash: {}, txResult: {},status: {}", 
            ethWalletDetail.getTxHash(), ethWalletDetail.getTxResult(), status);
        if (status != 0) {
            ethWalletDetail.setTxResult(status);
            //userWalletDetailMapper.updateByHash(ethWalletDetail.getTxHash(),status);
            ErrorCode result = getExchangeResult(ethWalletDetail);
            log.info("update Result:",result.getMessage());
        } else {
          if (ethWalletDetail.getPrior() > 2880) {
            ethWalletDetail.setTxResult(0x4);
          }
        }
        ethWalletDetailMapper.update(ethWalletDetail);
      }
    }
  }
  
  @PostConstruct
  public void startJob() {
    JobDetail jobDetail = JobBuilder.newJob(EtherRedeemJob.class)
        .withIdentity("JobDetailA", "groupA").build();
    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("triggerA", "groupA")
        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
            .withIntervalInSeconds(coinClient.getTxInterval()
        ).repeatForever()).startNow().build();
    jobDetail.getJobDataMap().put("service", this);
    schedulerService.addJob(jobDetail, trigger);
    log.info("add ethRedeem job ...");
  }
  
  public static String toMnemonicList(List<String> mnemonic) {
    JSONArray jsonArray = new JSONArray();
    for (String s : mnemonic) {
      jsonArray.add(s);
    }
    return jsonArray.toJSONString();
  }
  
  public String getActiveNet() {
    return coinClient.getNetName();
  }
  
  public static List<String> fromMnemonicList(String mnemonic) {
    JSONArray jsonArray = JSONArray.parseArray(mnemonic);
	List<String> x = new ArrayList<>();
    for (Integer i = 0; i< jsonArray.size(); i++) {
      x.add(jsonArray.getString(i));
    }
    return x;
  }
  
  public static List<String> fromEncryptedMnemonicList(String encryptedMnemonic){
    if (encryptedMnemonic == null) {
      return null;
    }
    String mnemonic = CryptoUtils.decrypt(encryptedMnemonic, KEY_SALT);
    return fromMnemonicList(mnemonic);
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
    
    String time = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
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
    userCoin.setUnconfirmedBalance(ethWallet.getUnconfirmedBalance());
    userCoin.setName(digitalCoin.getName());
    userCoin.setSymbol(digitalCoin.getSymbol());
    
    Double ethBalance = this.getEthBalance(userUuid, digitalCoin.getWeiFactor());
    BigDecimal balance=this.getBalance(userUuid, userCoin.getContract(),userCoin.getWeiFactor());
    userCoin.setEthBalance(ethBalance);
    userCoin.setBalance(balance);
    userCoin.setValue(this.getValue(balance).setScale(2, BigDecimal.ROUND_HALF_UP));
    return userCoin;
  }

  public List<UserCoin> listCoin(String userUuid){
    List<UserCoin> userCoinList = new ArrayList<>();
    UserCoin userCoin = getUserCoin(userUuid);
    if (userCoin != null)
      userCoinList.add(userCoin);
    return userCoinList;
  }
  
  //获取eth币的usercoin
  public UserCoin getEthCoinTemporary(UserCoin userCoin) {
	if(userCoin == null) return null;
    UserCoin ethCoin = new UserCoin();
    ethCoin.setBalance(new BigDecimal(userCoin.getEthBalance()));
    String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
    ExchangeRateModel oasModel = exchangeRateService.getRate(now, CurrencyCode.CNY);
    ExchangeRateModel ethModel = exchangeRateService.getRate(now, CurrencyCode.ETH);
    if(oasModel!=null && ethModel!=null) {
    	ethCoin.setValue((ethCoin.getBalance().multiply(oasModel.getRate()).multiply(ethModel.getRate())).setScale(2,BigDecimal.ROUND_HALF_UP));
    	return ethCoin;
    }
	return null;
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
    ethWalletDetail.setTxResult(0);
    ethWalletDetail.setTxHash(txHash);
    ethWalletDetail.setTxNetwork(coinClient.getNetName());
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
	  //当前金额加进待确认交易里面
	  EthWallet emptyEthWallet = new EthWallet();
	  emptyEthWallet.setUserUuid(ethWallet.getUserUuid()); 
	  emptyEthWallet.setUnconfirmedBalance(amount.add(ethWallet.getUnconfirmedBalance()));
	  Integer eResult = ethWalletMapper.update(emptyEthWallet);
	  if(eResult == 0) {
	   	returnValue.setErrorCode(ErrorCode.UPDATE_FAILED);
        return returnValue;
	  }
      addDetail(ethWallet.getAddress(), EthWalletDetailScope.TRANSFER_OUT,amount, txHash, comment, toUserAddress);
      addDetail(toUserAddress, EthWalletDetailScope.TRANSFER_IN, amount, txHash, comment, ethWallet.getAddress());
    }
    returnValue.setErrorCode(ErrorCode.SUCCESS);
    returnValue.setData(txHash);
    return returnValue;
  }
  /**
      *  用户的交易钱包转账
   * @param userUuid 发起转账的用户id
   * @param toUserAddress 目的交易钱包的地址
   * @param onlineWalletName 用户在线钱包地址
   * @param userCoin 发起转账的usecoin
   * @param amount 
   * @param gasPrice 
   * @param gasLimit
   * @param comment
   * @return
   */
  public ReturnValue<String> systemTransfer(Boolean flag,String userUuid, String toUserAddress,String onlineWalletName,
		  UserCoin userCoin,BigDecimal amount, BigInteger gasPrice, BigInteger gasLimit, String comment) {
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
	    	if(flag) {
	    		 addDetail(toUserAddress, EthWalletDetailScope.COIN_TO_ETH, amount, txHash, comment, "");
	    		 addDetail(ethWallet.getAddress(), EthWalletDetailScope.TRANSFER_OUT, amount, txHash, comment, toUserAddress);//system的记录
	    	}else {
	    		 addDetail(ethWallet.getAddress(), EthWalletDetailScope.ETH_TO_COIN, amount, txHash, comment, "");//onlineWalletName
	    		 addDetail(toUserAddress, EthWalletDetailScope.TRANSFER_IN, amount, txHash, comment, ethWallet.getAddress());//system的记录
	    	}
	     
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
  
  public ErrorCode reverseWithdraw(EthWalletTransfer info,UserModel user) {
	  OasDetail oasDetail = new OasDetail();
	  String uuid = UuidUtils.getPrefixUUID(UuidPrefix.OAS_DETAIL);
	  oasDetail.setUuid(uuid);
	  oasDetail.setValue(info.getAmount());
	  oasDetail.setUserUuid(user.getUuid());
	  oasDetail.setRemark(info.getRemark());
	  String now = DateUtils.dateTimeNow();
	  oasDetail.setCreated(now);
	  oasDetail.setUpdated(now);
	  oasDetail.setStatus(OasEventEnum.FORSURE.getCode());
	  oasDetail.setType(OasEventEnum.OAS_IN.getCode());
	  //插入充币提币表
	  Integer oResult = oasDetailMapper.insertSelective(oasDetail);
	  if(oResult == 0) {
		  return ErrorCode.UPDATE_FAILED;
	  }
	  
	  //查询system账号，给system转oas
	  UserModel systemInfo = oasDetailMapper.getSystemUserInfo();
	  if(systemInfo!=null) {
		  EthWallet systemEthWallet = this.getEthWalletByUserUuid(systemInfo.getUuid());//system的交易钱包
		  if(systemEthWallet == null) {
			  return ErrorCode.NO_ETH_WALLET;
		  }
		  UserCoin tokenCoin = this.getUserCoin(user.getUuid());
		  ReturnValue<String> ethInfo = systemTransfer(false,user.getUuid(),systemEthWallet.getAddress(),user.getName(), tokenCoin,info.getAmount(), info.getGasPrice(), info.getGasLimit(),info.getRemark());
		  if(ethInfo == null || ethInfo.getData() == null) {
			  log.info("hash null reason:",ethInfo.getErrorCode());
			  return ErrorCode.ETH_RETURN_HASH;
		  }
		  //update oasdetail hash值
		  Integer oasResult = oasDetailMapper.setWithdrawResultByUuid(uuid,null,DateUtils.dateTimeNow(),ethInfo.getData());
		  if(oasResult == 0) {
			  return ErrorCode.UPDATE_FAILED;
		  }
		  //用户交易钱包待确认交易+100
		  EthWallet userEthWallet = ethWalletMapper.selectByUserUuid(user.getUuid());
		  if(userEthWallet == null) {
			  return ErrorCode.NO_ETH_WALLET;
		  }
		  EthWallet emptyEth = new EthWallet();
		  emptyEth.setUnconfirmedBalance(userEthWallet.getUnconfirmedBalance().add(info.getAmount()));
		  emptyEth.setUpdated(DateUtils.dateTimeNow());
		  emptyEth.setUserUuid(userEthWallet.getUserUuid());
		  Integer eResult = ethWalletMapper.update(emptyEth);
		  if(eResult == 0) {
			  return ErrorCode.UPDATE_FAILED;
		  }
		  
	  }else {
		  return ErrorCode.SYSTEM_NOT_EXIST;
	  }
	  return ErrorCode.SUCCESS;
  }

  //在交易钱包的交易记录中点击记录
  public ErrorCode getExchangeResult(EthWalletDetail detail) {
	  //EthWalletDetail detail = ethWalletDetailMapper.selectByUUid(ethDetail.getUuid());//
	  if(detail.getTxHash() == null || detail.getTitle()== null ) {
		  return ErrorCode.SELECT_EMPTY;
	  } 
	  //非进行中的事件不用重新查看结果
	 /* if(detail.getStatus()!=0) {
		  return ErrorCode.SUCCESS;
	  }*/
	 
	  String hash = detail.getTxHash();
	  BigDecimal value = detail.getValue();
	  UserModel user = ethWalletDetailMapper.selectUserByAddress(detail.getAddress());
	  //UserModel user = ShiroUtils.getUser();	
	  if(user == null || user.getUuid() == null) {
		  return ErrorCode.UNLOGIN_FAILED;
	  }
	 
	  EthWallet userEthWallet = ethWalletMapper.selectByUserUuid(user.getUuid());
	  if(userEthWallet == null) {
        return ErrorCode.NO_ETH_WALLET;
	  }
	  //交易是否成功
	  Integer flagInt = detail.getTxResult();
	  String flag = "";
	  switch(flagInt){
	   case 0:  flag = "pending";break;
	   case 1:  flag = "success";break;
	   default: flag = "fail"; break;
	  }
	  //String flag = EthWalletService.getTransferResult("ropsten",hash);//"0xed8df5635ba8999bc547970b843a3ee87d8282032616637e983127f9f083fa5c"
	  if(flag.equals("success") || flag.equals("fail")) {
		  if(detail.getTitle().indexOf("转出")!=-1 || detail.getTitle().indexOf("转入")!=-1) {
			  EthWalletDetail updateDetail = new EthWalletDetail();
			  updateDetail.setUuid(detail.getUuid());
			  updateDetail.setTxResult(flag.equals("success")?OasEventEnum.EXCHANGE_SUCCESS.getCode():OasEventEnum.EXCHANGE_FAILED.getCode());
			  Integer upResult = ethWalletDetailMapper.updateByPrimaryKeySelective(updateDetail);
			  if(upResult == 0) {
				return ErrorCode.UPDATE_FAILED;
			}
		   //更新待确认交易
		   if(detail.getTitle().indexOf("转出")!=-1 ) {
			   if(userEthWallet.getUnconfirmedBalance().compareTo(detail.getValue()) == -1) {
				   return ErrorCode.UNCONFIRMED_BALANCE;
			   }
			   EthWallet emptyEthWallet = new EthWallet();
			   emptyEthWallet.setUserUuid(userEthWallet.getUserUuid());
			   emptyEthWallet.setUnconfirmedBalance(userEthWallet.getUnconfirmedBalance().subtract(detail.getValue()));
			   Integer eResult = ethWalletMapper.update(emptyEthWallet);
			   if(eResult == 0) {
			       return ErrorCode.UPDATE_FAILED;
			   }
		   }
		   return ErrorCode.SUCCESS;	
		  }
		  OasDetail oasDetail = new OasDetail();
	  	  oasDetail.setTxHash(hash);
	  	  oasDetail.setStatus(flag.equals("success")?OasEventEnum.SUCCESS.getCode():OasEventEnum.FAILED.getCode());
	  	  //修改提币记录充币表该记录事件状态
	  	  Integer oResult = oasDetailMapper.updateRecordByHash(oasDetail);	  
	  	  if(oResult == 0) {
	  		return ErrorCode.UPDATE_FAILED;
	  	  }
	  	 OasDetail oasD = oasDetailMapper.selectRecordByHash(hash);
	  	 if(oasD == null) {
	  		 return ErrorCode.SELECT_EMPTY;
	  	 }
	  	 UserWallet myWallet = userWalletMapper.selectByUserUuid(user.getUuid());
	  	 if(myWallet == null) {
	  		 return ErrorCode.NO_ONLINE_ACCOUNT;
	  	 }
	  	//system转给用户在线钱包oas
  		 UserModel systemInfo = oasDetailMapper.getSystemUserInfo();
  		 if(systemInfo == null) {
  			return ErrorCode.SYSTEM_NOT_EXIST;
  		 }
  		 //system钱包
  		 UserWallet systemWallet = userWalletMapper.selectByUserUuid(systemInfo.getUuid());
  		 if(systemWallet == null) {
  			 return ErrorCode.NO_ONLINE_ACCOUNT;
  		 }
  		/* if(systemWallet.getBalance().compareTo(value) == -1) {
  			 return ErrorCode.BALANCE_NOT_ENOUGH;
  		 }*/
	  	 //提币
	  	 if(oasD.getType().equals(OasEventEnum.OAS_OUT.getCode())) {
	  		 if(flag.equals("success")) {
	  			Integer tResult = userWalletMapper.changeBalanceAndUnconfimed(oasD.getUserUuid(),null,myWallet.getUnconfirmedBalance().subtract(value),DateUtils.dateTimeNow());
	  			Integer sResult = userWalletMapper.increaseBalance(systemWallet.getUuid(), value);
	  			if(tResult == 0 || sResult == 0) {
	  		  		 return ErrorCode.UPDATE_FAILED;
	  		  	}
	  		 }else {
	  			/* if(systemWallet.getBalance().compareTo(oasD.getExtra()) == -1) {
	  				  return ErrorCode.OAS_EXTRA_MONEY_NOT_ENOUGH;
	  			  }*/
	  			 
	  			Integer tResult = userWalletMapper.changeBalanceAndUnconfimed(oasD.getUserUuid(),myWallet.getBalance().add(value).add(oasD.getExtra()),myWallet.getUnconfirmedBalance().subtract(value),DateUtils.dateTimeNow());
	  			Integer sResult = userWalletMapper.decreaseBalance(systemWallet.getUuid(), oasD.getExtra());
	  			if(tResult == 0 || sResult == 0) {
	  		  		 return ErrorCode.UPDATE_FAILED;
	  		  	}
	  		 }
	  		//更新在线钱包记录中的状态
	  		Integer uwdResult = userWalletDetailMapper.updateByOasDetailUuid(flagInt,oasD.getUuid());
	  		if(uwdResult == 0) {
 		  		 return ErrorCode.UPDATE_FAILED;
 		  	}

	  	 }else {
	  		 //充币
		  	 if(flag.equals("success")) {
		  		 Integer sysResult = userWalletMapper.decreaseBalance(systemWallet.getUuid(), value);
		  		 Integer myResult = userWalletMapper.increaseBalance(myWallet.getUuid(), value);
		  		 if(sysResult==0 || myResult == 0) {
		  			 return ErrorCode.UPDATE_FAILED;
		  		 }
		  		 //在线钱包中显示记录
		  		 userWalletDetailMapper.insertSelective(UserWalletService.setDetail(myWallet, "", UserWalletDetailScope.ETH_TO_COIN, value, detail.getRemark(), detail.getRemark(),oasD.getUuid()));//detail.getTxHash(),coinClient.getNetName()
		  		 userWalletDetailMapper.insertSelective(UserWalletService.setDetail(systemWallet, user.getName(), UserWalletDetailScope.TRANSFER_OUT, value, detail.getRemark(), detail.getRemark(),null));//system转出
				 
		  	 }
		  	 	//修改待确认交易
				EthWallet emptyEth = new EthWallet();
				/*if(userEthWallet.getUnconfirmedBalance().compareTo(value) == -1) {
					return ErrorCode.UNCONFIRMED_BALANCE;
				}*/
				emptyEth.setUnconfirmedBalance(userEthWallet.getUnconfirmedBalance().subtract(value));
				emptyEth.setUserUuid(user.getUuid());
				Integer eResult = ethWalletMapper.update(emptyEth);
				if(eResult == 0) {
					return ErrorCode.UPDATE_FAILED;
				}
				
	  	 }
	  /*	EthWalletDetail updateDetail = new EthWalletDetail();
		updateDetail.setUuid(detail.getUuid());
		updateDetail.setStatus(flag.equals("success")?OasEventEnum.EXCHANGE_SUCCESS.getCode():OasEventEnum.EXCHANGE_FAILED.getCode());
		Integer upResult = ethWalletDetailMapper.updateByPrimaryKeySelective(updateDetail);
		if(upResult == 0) {
			return ErrorCode.UPDATE_FAILED;
		}*/
	  
	  }
  	  return ErrorCode.SUCCESS;
  }
  
  public static Integer getTransferResult(String net,String hash) {
	  Integer flag = 0;//"pending";
	  String result = HttpUtils.sendPost("https://"+net+".etherscan.io/tx/"+hash,null);
  	  result = result.replaceAll("//&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "");//去掉网页中带有html语言的标签
  	  if(result.indexOf("TxReceipt Status:Success")!=-1){
  		flag = 1;//"success";
  	  }
  	  if(result.indexOf("TxReceipt Status:Fail")!=-1) {
  		flag = 2;//"fail";
  	  }
  	  return flag;
  }
  
  public ErrorCode backupEthWallet(String userUuid, BackupEthWallet backupEthWallet){
	  EthWallet ethWallet = getEthWalletByUserUuid(userUuid);
	  if (ethWallet == null)
		  return ErrorCode.NO_ETH_WALLET;
	  String key = ethWallet.getPrivateKey();
	  String mnemonicList = ethWallet.getMnemonicList();
	  
	  if (ethWallet.getCrypto() != 0) {
		  key = CryptoUtils.decrypt(key, KEY_SALT);
		  mnemonicList = CryptoUtils.decrypt(mnemonicList, KEY_SALT);
	  }
	  backupEthWallet.setPrivateKey(key);
	  backupEthWallet.setMnemonicList(fromMnemonicList(mnemonicList));
	  return ErrorCode.SUCCESS;
  }
}



