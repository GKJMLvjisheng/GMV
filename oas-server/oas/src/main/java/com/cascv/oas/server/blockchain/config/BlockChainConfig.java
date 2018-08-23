package com.cascv.oas.server.blockchain.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class BlockChainConfig {
  
  @Value("${tokenClient.url}")
  @Setter @Getter private String url;
  
  @Value("${tokenClient.contractAddress}")
  @Setter @Getter private String contractAddress;

  @Bean
  public TokenClient getTokenClient() {
    log.info("blockchain url is {}", url);
    log.info("blockchain contractAddress is {}", contractAddress);
    Web3j web3j =  Web3j.build(new HttpService(url));
    TokenClient tokenClient = new TokenClient();
    tokenClient.setWeb3j(web3j);
    tokenClient.setContractAddress(contractAddress);
    return tokenClient;
  }
}
