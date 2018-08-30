package com.cascv.oas.server.blockchain.service;

import java.math.BigDecimal;
import java.util.List;

import com.cascv.oas.server.blockchain.config.CoinClient;
import com.cascv.oas.server.blockchain.mapper.DigitalCoinMapper;
import com.cascv.oas.server.blockchain.model.DigitalCoin;
import com.cascv.oas.server.blockchain.wrapper.ContractSymbol;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DigitalCoinService {
    @Autowired
    private DigitalCoinMapper digitalCoinMapper;

    public List<DigitalCoin> listDigitalCoins() {
      return digitalCoinMapper.selectAll();
    }
    

    public DigitalCoin find(String contract) {
      return digitalCoinMapper.selectByContract(contract);
    }
    
    public ContractSymbol selectContractSymbol(String userUuid) {
		return digitalCoinMapper.selectContractSymbol(userUuid);
    }

    public Integer create(CoinClient coinClient, String contract){
      Integer ret = 0;
      try {
        DigitalCoin digitalCoin = this.find(contract);
        if (digitalCoin != null) {
          log.info("coin {} already loaded", contract);
          return 0;
        }
        digitalCoin = new DigitalCoin();
        digitalCoin.setContract(contract);
        BigDecimal weiFactor = coinClient.weiFactorOf(contract);
        digitalCoin.setWeiFactor(weiFactor);
        digitalCoin.setName(coinClient.nameOf(contract));
        digitalCoin.setSymbol(coinClient.symbolOf(contract));
        BigDecimal supply = coinClient.supplyOf(contract, weiFactor);
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
}