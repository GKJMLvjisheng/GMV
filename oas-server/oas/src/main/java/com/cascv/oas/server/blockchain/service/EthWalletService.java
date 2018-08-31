package com.cascv.oas.server.blockchain.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

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
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.config.CoinClient;
import com.cascv.oas.server.blockchain.config.ExchangeParam;
import com.cascv.oas.server.blockchain.config.TransferQuota;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.mapper.EthWalletMapper;
import com.cascv.oas.server.blockchain.mapper.UserCoinMapper;
import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.model.EthWallet;
import com.cascv.oas.server.blockchain.model.UserCoin;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;
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
  private ExchangeParam exchangeParam;
  
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
      List<String> checkMnemonic = MnemonicCode.INSTANCE.toMnemonic(checkMnemonicSeedBytes);
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
      ethWallet.setKeystore(jsonStr);
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
  
  public ContractSymbol selectContractSymbol(String userUuid) {
	  return digitalCoinService.selectContractSymbol(userUuid);
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

  public EthWallet getEthWalletByUserUuid(String userUuid)
  {
    return ethWalletMapper.selectByUserUuid(userUuid);
  }

  public BigDecimal getBalance(String userUuid, String contract, BigDecimal weiFactor) {
    BigDecimal balance = BigDecimal.ZERO;
    try {
      EthWallet ethWallet = ethWalletMapper.selectByUserUuid(userUuid);
      balance = coinClient.balanceOf(ethWallet.getAddress(), contract, weiFactor);
      log.info("getBalance of {}", balance);
    } catch (Exception e) {

    }
    return balance;
  }
  
  public BigDecimal getValue(BigDecimal balance) {
    BigDecimal rate = BigDecimal.valueOf(exchangeParam.getTokenRmbRate());
    return balance.multiply(rate);
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

  public ErrorCode transfer(String userUuid, String contract, String toAddress, BigDecimal amount) {

    EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
    if (ethWallet == null)
      return ErrorCode.NO_ETH_WALLET;
    if (contract == null)
      contract = coinClient.getToken();
    UserCoin userCoin = this.getUserCoin(ethWallet.getUserUuid(), contract);
    if (userCoin.getBalance().compareTo(amount) < 0)
      return ErrorCode.BALANCE_NOT_ENOUGH;
    amount = amount.multiply(userCoin.getWeiFactor());
    String txHash=coinClient.transfer(ethWallet.getAddress(), ethWallet.getPrivateKey(), toAddress, contract, amount.toBigInteger());
    log.info("txhash {}", txHash);
    return ErrorCode.SUCCESS;
  }
  
  public ErrorCode multiTransfer(String userUuid, String contract, List<TransferQuota> quota) {
    EthWallet ethWallet = this.getEthWalletByUserUuid(userUuid);
    if (ethWallet == null)
      return ErrorCode.NO_ETH_WALLET;
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
    if (userCoin.getBalance().compareTo(total) < 0)
      return ErrorCode.BALANCE_NOT_ENOUGH;
    String txHash=coinClient.multiTransfer(ethWallet.getAddress(), ethWallet.getPrivateKey(), addressList, contract, amountIntList);
    log.info("txhash {}", txHash);
    if (txHash!=null)
      return ErrorCode.SUCCESS;
    else
      return ErrorCode.MULTIPLE_TRANSFER_FAILURE;
  }
}



