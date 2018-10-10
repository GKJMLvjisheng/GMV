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
        
    Map<String, Web3j> network = new HashMap<>();
    List<EthNetworkModel> ethNetworkModelList = digitalCoinService.listEthNetwork();
    for (EthNetworkModel ethNetworkModel : ethNetworkModelList) {
      log.info("network {} - {}", ethNetworkModel.getName(), ethNetworkModel.getProvider());
      Web3j web3j =  Web3j.build(new HttpService(ethNetworkModel.getProvider()));
      network.put(ethNetworkModel.getName(), web3j);
    }
    coinClient.setNetwork(network);
    
    List<EthContractModel> ethContractModelList = digitalCoinService.listEthContract();
    coinClient.setEthContract(ethContractModelList);
    
    for (EthContractModel ethContractModel:ethContractModelList) {
      log.info("contract {} - {}", ethContractModel.getName(), ethContractModel.getAddress());
      coinClient.setDefaultNet(ethContractModel.getNetwork());
      digitalCoinService.create(coinClient, ethContractModel);
    }
    
    EthConfigModel ethConfigModel = digitalCoinService.getEthConfig();
    log.info("config network {}", ethConfigModel.getActiveNetwork());
    coinClient.setDefaultNet(ethConfigModel.getActiveNetwork());
    return coinClient;
  }
}
