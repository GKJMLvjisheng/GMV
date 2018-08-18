package com.cascv.oas.server.blockchain.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;
import org.web3j.protocol.admin.methods.response.PersonalListAccounts;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;

import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.blockchain.service.WalletService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/web3j")
@Slf4j
public class Web3jController {
  
  @Autowired
  private Web3j web3j;
  
  @Autowired
  private WalletService walletService;
  
  @PostMapping(value="/testApi")
  public ResponseEntity<?> testApi(String name){
    
    List<String> mn = walletService.generateMnemonic("12345678");
    
    for (String s : mn) {
      log.info("[ ] {}", s);
    }
//    listAccounts();
//    createNewAccount();
//    String version = clientVersion();
//    log.info("client version {}", version);
    
    return new ResponseEntity.Builder<Integer>().setData(0).setStatus(0).setMessage("ok").build();
  }
  
  
//  private void listAccounts() {
//    try {
//      PersonalListAccounts personalListAccounts = admin.personalListAccounts().send();
//      List<String> accountIds = personalListAccounts.getAccountIds();
//      for (String s : accountIds) {
//        log.info("account {}", s);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
  
//  private void createNewAccount() {
//    String password = "02641c2839f4432585736400d4405a11";
//    try {
//      NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
//      // String address = newAccountIdentifier.getAccountId();
//      String response = newAccountIdentifier.getRawResponse();
//      log.info("response {} " + response);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }
  
//  private String clientVersion() {
//    try {
//      Web3ClientVersion web3ClientVersion = web3j.web3ClientVersion().send();
//      
//      return web3ClientVersion.getWeb3ClientVersion();
//    } catch (IOException e) {
//      e.printStackTrace();
//      return "";
//    }
//  }
}
