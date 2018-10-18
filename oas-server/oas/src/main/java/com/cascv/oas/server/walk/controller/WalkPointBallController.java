package com.cascv.oas.server.walk.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.energy.vo.EnergyBallTakenResult;
import com.cascv.oas.server.energy.vo.EnergyBallTokenRequest;
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
	
	 @PostMapping(value = "/inquireWalkPointBall")  
	 @ResponseBody
	 public ResponseEntity<?> inquireWalkPointBall(@RequestBody StepNumWrapper stepNumWrapper){
		 log.info("时间"+stepNumWrapper.getQuota().get(0).getDate());
		 log.info("步数"+stepNumWrapper.getQuota().get(0).getStepNum().toString());
		 String userUuid = ShiroUtils.getUserUuid();
		 log.info("userUuid={}",userUuid);
		 List<WalkBallReturn> walkBallReturnList = walkService.inquireWalkPointBall(userUuid, stepNumWrapper.getQuota());
		 
		return new ResponseEntity.Builder<List<WalkBallReturn>>()
				.setData(walkBallReturnList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		 
	 }
	 
	 
	 @PostMapping(value = "/takeWalkPointBall")  
	 @ResponseBody
	 public ResponseEntity<?> takeWalkPointBall(@RequestBody EnergyBallTokenRequest energyBallTokenRequest) throws ParseException{
		 String userUuid = ShiroUtils.getUserUuid();
		 ErrorCode errorCode = ErrorCode.GENERAL_ERROR;
		 EnergyBallTakenResult energyBallTakenResult=new EnergyBallTakenResult();
		 
		 //判断是否具有获取奖励得权限
		 if(permService.getWalkPerm()){
		     energyBallTakenResult = walkService.takeWalkPointBall(userUuid, energyBallTokenRequest.getBallId());
		     log.info("you have the permission");
		     if(energyBallTakenResult != null)
			     errorCode= ErrorCode.SUCCESS;
		 }	     	
		 
		 return new ResponseEntity.Builder<EnergyBallTakenResult>()
				.setData(energyBallTakenResult)
				.setErrorCode(errorCode)
				.build();		 
	 }
}
