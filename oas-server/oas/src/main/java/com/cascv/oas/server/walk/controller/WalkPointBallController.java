package com.cascv.oas.server.walk.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.ActivityRewardConfig;
import com.cascv.oas.server.energy.mapper.EnergyWalletMapper;
import com.cascv.oas.server.energy.vo.EnergyBallTakenResult;
import com.cascv.oas.server.energy.vo.EnergyBallTokenRequest;
import com.cascv.oas.server.log.annotation.WriteLog;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.user.service.PermService;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.walk.service.WalkService;
import com.cascv.oas.server.walk.wrapper.StepNumWrapper;
import com.cascv.oas.server.walk.wrapper.WalkBallReturn;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/walkPoint")
@Slf4j
public class WalkPointBallController {
	
	@Autowired
	private WalkService walkService; 
	@Autowired
	private PermService permService;
	@Autowired
	private ActivityMapper activityMapper;
	@Autowired
	private EnergyWalletMapper energyWalletMapper;
	@Autowired
	private MinerMapper minerMapper;
	
	private static final Integer SYSTEM_PARAMETER_CURRENCY = 11;
	
	 @PostMapping(value = "/inquireWalkPointBall")  
	 @ResponseBody
	 @WriteLog(value="WalkBall")
	 public ResponseEntity<?> inquireWalkPointBall(@RequestBody StepNumWrapper stepNumWrapper){
		 String name = ShiroUtils.getUser().getName();
		 for(int i=0; i<stepNumWrapper.getQuota().size(); i++) {
			 log.info(name+"Time={}"+stepNumWrapper.getQuota().get(i).getDate(), "stepNum={}"+stepNumWrapper.getQuota().get(i).getStepNum().toString());
		 }
		 
		 String userUuid = ShiroUtils.getUserUuid();
		 List<WalkBallReturn> walkBallReturnList = walkService.inquireWalkPointBall(userUuid, stepNumWrapper.getQuota());
		 log.info("walklist={}",JSON.toJSONString(walkBallReturnList));
		return new ResponseEntity.Builder<List<WalkBallReturn>>()
				.setData(walkBallReturnList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		 
	 }
	 
	 @PostMapping(value = "/inquireWalkBallMaxValue")  
	 @ResponseBody
	 public ResponseEntity<?> inquireWalkBallMaxValue(){
		 String userUuid = ShiroUtils.getUserUuid();
		 ActivityRewardConfig activityRewardConfig = activityMapper.selectBaseValueBySourceCodeAndRewardCode("WALK", "POINT");
		 BigDecimal β = minerMapper.selectSystemParameterByCurrency(SYSTEM_PARAMETER_CURRENCY).getParameterValue();
		 BigDecimal maxValue = BigDecimal.ZERO;
		 if(activityRewardConfig != null) {
			 BigDecimal value = activityRewardConfig.getMaxValue();
			 BigDecimal power = BigDecimal.ONE;
		     if(energyWalletMapper.selectByUserUuid(userUuid).getPower().compareTo(BigDecimal.ZERO) != 0) {
		        power = energyWalletMapper.selectByUserUuid(userUuid).getPower();
		     }
			 maxValue = value.multiply(power).divide(β, 2, BigDecimal.ROUND_HALF_UP);
		 }
		 log.info("walkMaxValue={}", maxValue);
		 return new ResponseEntity.Builder<BigDecimal>()
				 .setData(maxValue)
				 .setErrorCode(ErrorCode.SUCCESS)
				 .build();
	 }
	 
	 
	 @PostMapping(value = "/takeWalkPointBall")  
	 @ResponseBody
	 public ResponseEntity<?> takeWalkPointBall(@RequestBody EnergyBallTokenRequest energyBallTokenRequest) throws ParseException{
		 String userUuid = ShiroUtils.getUserUuid();
		 ErrorCode errorCode = ErrorCode.SUCCESS;
		 EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
		 log.info("walkBallId={}",energyBallTokenRequest.getBallId());
		 //判断是否具有获取奖励得权限
		 if(permService.getWalkPerm()){
		     energyBallTakenResult = walkService.takeWalkPointBall(userUuid, energyBallTokenRequest.getBallId());
		     log.info("you have the permission");
		     
		     if(energyBallTakenResult == null)
			     errorCode= ErrorCode.GENERAL_ERROR;
		 }else{
			  errorCode = ErrorCode.NO_WALK_PERMISSION;
		 }	     	
		 
		 return new ResponseEntity.Builder<EnergyBallTakenResult>()
				.setData(energyBallTakenResult)
				.setErrorCode(errorCode)
				.build();		 
	 }
}
