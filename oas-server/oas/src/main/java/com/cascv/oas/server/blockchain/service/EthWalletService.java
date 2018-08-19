package com.cascv.oas.server.blockchain.service;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
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

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.blockchain.mapper.EthHdWalletMapper;
import com.cascv.oas.server.blockchain.model.EthHdWallet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EthWalletService {
  public static final String ETH_TYPE = "m/44'/60'/0'/0/0"; 
  
  private static SecureRandom secureRandom = new SecureRandom();
  
  @Autowired
  private EthHdWalletMapper ethHdWalletMapper;
  
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
      System.out.println("验证助记词 " + Arrays.toString(checkMnemonic.toArray()));
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
  
  
  public EthHdWallet create(Integer userId, String password){
    
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
      System.out.println("eth address " + "0x" + walletFile.getAddress());
      ObjectMapper objectMapper = ObjectMapperFactory.getObjectMapper();
      String jsonStr = objectMapper.writeValueAsString(walletFile);
      EthHdWallet ethHdWallet =  new EthHdWallet();
      ethHdWallet.setUserId(userId);
      ethHdWallet.setMnemonicList(EthHdWallet.toMnemonicList(ds.getMnemonicCode()));
      ethHdWallet.setPublicKey(keyPair.getPublicKey().toString(16));
      ethHdWallet.setPrivateKey(keyPair.getPrivateKey().toString(16));
      ethHdWallet.setMnemonicPath(dkKey.getPathAsString());
      ethHdWallet.setAddress("0x" + walletFile.getAddress());
      ethHdWallet.setKeystore(jsonStr);
      String datetime = DateUtils.dateTimeNow();
      ethHdWallet.setCreated(datetime);
      ethHdWallet.setUpdated(datetime);
      ethHdWalletMapper.insertSelective(ethHdWallet);
      return ethHdWallet;
    } catch (CipherException | JsonProcessingException e) {
      e.printStackTrace();
      return null;
    }
    
  }
}
