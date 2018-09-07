package com.cascv.oas.server.blockchain.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.web3j.protocol.Web3j;
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
  
  @Setter @Getter private Map<String,String> providers;
  @Setter @Getter private String defaultNet;
  @Setter @Getter private String token;
  @Setter @Getter private List<CoinContract> contracts;

  @Bean
  @Lazy
  public CoinClient getCoinClient() {
    

    log.info("blockchain token is {}", token);
    CoinClient coinClient = new CoinClient();
    
    Map<String, Web3j> providerMap = new HashMap<>();
	for (String p : providers.keySet()) {
		log.info("net {} provided by {}", p, providers.get(p));
		Web3j web3j =  Web3j.build(new HttpService(providers.get(p)));
		providerMap.put(p, web3j);
	}
	coinClient.setProviderMap(providerMap);
	if (providerMap.get(defaultNet) != null)
		coinClient.setDefaultNet(defaultNet);
    coinClient.setToken(token);
    
    for (CoinContract s : contracts) {
      log.info("suport coin '{}'", s.getAddress());
      digitalCoinService.create(coinClient, s.getAddress());
    }
    return coinClient;
  }
}
