package com.cascv.oas.server.blockchain.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.NewAccountIdentifier;

import com.cascv.oas.core.common.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value="/web3j")
@Slf4j
public class Web3jController {
  
  //@Autowired
  //private Web3j web3j;
  
  @Autowired
  private Admin admin;
  
  @PostMapping(value="/testApi")
  public ResponseEntity<?> testApi(String name){
    
    String address = createNewAccount();
    log.info("new accounting {}", address);
    return new ResponseEntity.Builder<Integer>().setData(0).setStatus(0).setMessage("ok").build();
  }
  
  
  private String createNewAccount() {
    String password = "123";
    try {
      NewAccountIdentifier newAccountIdentifier = admin.personalNewAccount(password).send();
      String address = newAccountIdentifier.getAccountId();
      return address;
    } catch (IOException e) {
      e.printStackTrace();
      return "";
    }
  }
}
