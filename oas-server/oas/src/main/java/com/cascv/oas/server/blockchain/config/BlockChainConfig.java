package com.cascv.oas.server.blockchain.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

class ContractConfig {
  @Setter @Getter private String address;
};

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "wallet")
public class BlockChainConfig {
  
  
  @Setter @Getter private String provider;
  @Setter @Getter private List<ContractConfig> contracts;
  
  @Bean
  public TokenClient getTokenClient() {
    
    String address = contracts.get(0).getAddress();
    log.info("blockchain url is {}", provider);
    log.info("blockchain contractAddress is {}", address);
    Web3j web3j =  Web3j.build(new HttpService(provider));
    TokenClient tokenClient = new TokenClient();
    tokenClient.setWeb3j(web3j);
    tokenClient.setContractAddress(address);
    return tokenClient;
  }
}
