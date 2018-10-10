package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;
import java.util.List;

import com.cascv.oas.server.blockchain.config.CoinClient;
import com.cascv.oas.server.blockchain.mapper.DigitalCoinMapper;
import com.cascv.oas.server.blockchain.mapper.EthConfigMapper;
import com.cascv.oas.server.blockchain.mapper.EthContractMapper;
import com.cascv.oas.server.blockchain.mapper.EthNetworkMapper;
import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.model.EthConfigModel;
import com.cascv.oas.server.blockchain.model.EthContractModel;
import com.cascv.oas.server.blockchain.model.EthNetworkModel;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DigitalCoinService {
    
    @Autowired
    private DigitalCoinMapper digitalCoinMapper;

    @Autowired
    private EthNetworkMapper ethNetworkMapper;
    
    @Autowired
    private EthContractMapper ethContractMapper;
    
    @Autowired
    private EthConfigMapper ethConfigMapper;
    
    
    public List<DigitalCoin> listDigitalCoins() {
      return digitalCoinMapper.selectAll();
    }
    

    public DigitalCoin find(String contract) {
      return digitalCoinMapper.selectByContract(contract);
    }
    
    public List<ContractSymbol> selectContractSymbol(String name) {
		return digitalCoinMapper.selectContractSymbol(name);
    }

    public Integer create(CoinClient coinClient, EthContractModel ethContractModel){
      Integer ret = 0;
      String contract = ethContractModel.getAddress();
      try {
        DigitalCoin digitalCoin = this.find(contract);
        if (digitalCoin != null) {
          log.info("coin {} already loaded", contract);
          return 0;
        }
        digitalCoin = new DigitalCoin();
        digitalCoin.setContract(contract);
        digitalCoin.setNetwork(ethContractModel.getNetwork());
        BigDecimal weiFactor = coinClient.weiFactorOf();
        digitalCoin.setWeiFactor(weiFactor);
        digitalCoin.setName(coinClient.nameOf());
        digitalCoin.setSymbol(coinClient.symbolOf());
        BigDecimal supply = coinClient.supplyOf(weiFactor);
        digitalCoin.setSupply(supply);
        ret=digitalCoinMapper.insertSelective(digitalCoin);
        log.info("new coin name {} symbol {} weiFactor {} contract {} supply {} loaded",
              digitalCoin.getName(), 
              digitalCoin.getSymbol(),
              digitalCoin.getWeiFactor(),
              digitalCoin.getContract(), 
              digitalCoin.getSupply());
      } catch (Exception e) {
        log.info("load new coin {} failed", contract);
        ret = 0;
      }
      return ret;
    }
    // eth network
    public Integer addEthNetwork(EthNetworkModel ethNetworkModel){
      return ethNetworkMapper.insertSelective(ethNetworkModel);
    }
    
    public Integer deleteEthNetwork(String name) {
      return ethNetworkMapper.deleteByName(name);
    }
    
    public List<EthNetworkModel> listEthNetwork(){
      return ethNetworkMapper.selectAll();
    }
    
    // eth contract
    public Integer addEthContract(EthContractModel ethContractModel) {
      return ethContractMapper.insertSelective(ethContractModel);
    }
    
    public Integer deleteEthContract(String name) {
      return ethContractMapper.deleteByName(name);
    }

    public List<EthContractModel> listEthContract(){
      return ethContractMapper.selectAll();
    }
    
    public EthContractModel findEthContractByName(String name) {
      return ethContractMapper.selectByName(name);
    }
    // eth config
    
    public Integer addEthConfig(EthConfigModel ethConfigModel) {
      log.info("addEthConfig net {}", ethConfigModel.getActiveNetwork());
      return ethConfigMapper.insertSelective(ethConfigModel);
    }
    
    public Integer updateEthConfig(EthConfigModel ethConfigModel) {
      log.info("updateEthConfig net {}", ethConfigModel.getActiveNetwork());
      return ethConfigMapper.update(ethConfigModel);
    }
    
    public Integer deleteEthConfig(Integer id) {
      return ethConfigMapper.deleteById(id);
    }
    
    public EthConfigModel getEthConfig() {
      EthConfigModel ethConfigModel = ethConfigMapper.selectOne();
      if (ethConfigModel != null) {
        if (ethConfigModel.getActiveNetwork() == null) 
          ethConfigModel.setActiveNetwork("ropsten");
      }
      return ethConfigModel;
    }

}