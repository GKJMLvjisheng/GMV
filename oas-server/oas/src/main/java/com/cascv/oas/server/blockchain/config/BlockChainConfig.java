package com.cascv.oas.server.blockchain.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.cascv.oas.server.blockchain.model.EthConfigModel;
import com.cascv.oas.server.blockchain.model.EthContractModel;
import com.cascv.oas.server.blockchain.model.EthNetworkModel;
import com.cascv.oas.server.blockchain.service.DigitalCoinService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class BlockChainConfig {
  
  @Autowired 
  private DigitalCoinService digitalCoinService;
  
  @Setter @Getter private Map<String,String> providers;

  @Bean
  @Lazy
  public CoinClient getCoinClient() {
    

    CoinClient coinClient = new CoinClient();
        
    EthConfigModel ethConfigModel = digitalCoinService.getEthConfig();
    log.info("config {} - {}", ethConfigModel.getActiveNetwork(), ethConfigModel.getActiveToken());
    
    Map<String, Web3j> providerMap = new HashMap<>();
    List<EthNetworkModel> ethNetworkModelList = digitalCoinService.listEthNetwork();
    for (EthNetworkModel ethNetworkModel : ethNetworkModelList) {
      log.info("network {} - {}", ethNetworkModel.getName(), ethNetworkModel.getProvider());
      Web3j web3j =  Web3j.build(new HttpService(ethNetworkModel.getProvider()));
      providerMap.put(ethNetworkModel.getName(), web3j);
    }
    coinClient.setProviderMap(providerMap);
    
    String activeNetwork = ethConfigModel.getActiveNetwork();
    if (providerMap.get(activeNetwork) != null) {
      log.info("default network {}", activeNetwork);
      coinClient.setDefaultNet(activeNetwork);
    }
    
    List<EthContractModel> ethContractModelList = digitalCoinService.listEthContract();
    for (EthContractModel ethContractModel:ethContractModelList) {
      log.info("contract {} - {}", ethContractModel.getName(), ethContractModel.getAddress());
      digitalCoinService.create(coinClient, ethContractModel.getAddress());
    }
    EthContractModel ethContractModel= digitalCoinService.findEthContractByName(ethConfigModel.getActiveToken());
    
    log.info("token {} - {}", ethContractModel.getName(), ethContractModel.getAddress());
    coinClient.setToken(ethContractModel.getAddress());
    
    return coinClient;
  }
}
