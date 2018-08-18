package com.cascv.oas.server.blockchain.service;

import java.security.SecureRandom;
import java.util.List;

import org.bitcoinj.wallet.DeterministicSeed;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
  public static final String ETH_TYPE = "m/44'/60'/0'/0/0"; 
  
  private static SecureRandom secureRandom = new SecureRandom();
  
  public List<String> generateMnemonic(String password){
    String passphrase = "";
    long creationTimeSeconds = System.currentTimeMillis() / 1000;
    DeterministicSeed ds = new DeterministicSeed(secureRandom, 128, passphrase, creationTimeSeconds);
    return ds.getMnemonicCode();
  }
}
