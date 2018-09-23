package com.cascv.oas.server.activity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.model.EnergyBall;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActivityService {
	
	@Autowired
	private ActivityMapper activityMapper;
	
	public ActivityCompletionStatus selectUserActivityByUserUuid(String userUuid) {
		ActivityCompletionStatus activityCompletionStatus = activityMapper.selectUserActivityStatusByUserUuid(userUuid);
		return activityCompletionStatus;
		
	}
		
	public EnergyBall addEnergyBall() {
		return null;
		
	}
	
	public EnergyBall addEnergyTradeRecord() {
		return null;
		
	}
	
	public EnergyBall addEnergyWallet() {
		return null;
		
	}
	
	public EnergyBall addActivityCompletionStatus() {
		return null;
		
	}
	
	

}
