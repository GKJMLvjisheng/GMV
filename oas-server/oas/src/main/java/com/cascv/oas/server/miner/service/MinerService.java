package com.cascv.oas.server.miner.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.wrapper.UserMinerWrapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MinerService {
	
	@Autowired
	private MinerMapper minerMapper;
	
	public List<UserMinerWrapper> getUserMiner(String userUuid){
		List<UserMinerWrapper> userMinerList = minerMapper.selectByuserUuid(userUuid);
		return userMinerList;	
	}
	
	public BigDecimal getMinerEfficiency(String userUuid) {
		List<UserMinerWrapper> userMinerList = this.getUserMiner(userUuid);
		BigDecimal efficiencySum = BigDecimal.ZERO;
		for(int i=0; i<userMinerList.size(); i++) {
			
		}
		log.info("efficiencySum={}", efficiencySum);
		return efficiencySum;
	}

}
