package com.cascv.oas.server.blockchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BlockChainConfig {
  
  @Value("${web3j.url}")
  private String url;
  
  
  @Bean
  public Web3j getWeb3j() {
    log.info("blockchain url is {}", url);
    return Web3j.build(new HttpService(url));
  }
}
