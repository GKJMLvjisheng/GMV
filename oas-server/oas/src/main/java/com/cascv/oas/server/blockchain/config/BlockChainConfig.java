package com.cascv.oas.server.blockchain.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.http.HttpService;

import com.cascv.oas.server.blockchain.service.DigitalCoinService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

class CoinContract {
  @Setter @Getter private String address;
};

@Configuration
@Slf4j
@ConfigurationProperties(prefix = "wallet")
public class BlockChainConfig {
  
  @Autowired 
  private DigitalCoinService digitalCoinService;
  
  @Setter @Getter private String provider;
  @Setter @Getter private String token;
  @Setter @Getter private List<CoinContract> contracts;
  @Setter @Getter private ExchangeParam exchangeParam;

  @Bean
  @Lazy
  public CoinClient getCoinClient() {
    
    log.info("blockchain url is {}", provider);
    log.info("blockchain token is {}", token);

    
    CoinClient coinClient = new CoinClient();
    
    Web3j web3j =  Web3j.build(new HttpService(provider));
    coinClient.setWeb3j(web3j);

    Admin admin = Admin.build(new HttpService(provider));
    coinClient.setAdmin(admin);
    
    coinClient.setToken(token);
    
    for (CoinContract s : contracts) {
      log.info("suport coin '{}'", s.getAddress());
      digitalCoinService.create(coinClient, s.getAddress());
    }
    return coinClient;
  }

  @Bean
  public ExchangeParam getExchangeParam(){
    return exchangeParam;
  }

}
