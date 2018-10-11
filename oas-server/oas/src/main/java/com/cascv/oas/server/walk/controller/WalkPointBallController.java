package com.cascv.oas.server.walk.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.energy.vo.EnergyBallTakenResult;
import com.cascv.oas.server.energy.vo.EnergyBallWrapper;
import com.cascv.oas.server.utils.ShiroUtils;
import com.cascv.oas.server.walk.service.WalkService;
import com.cascv.oas.server.walk.wrapper.StepNumWrapper;
import com.cascv.oas.server.walk.wrapper.WalkBallReturn;
import com.cascv.oas.server.walk.wrapper.WalkBallTokenRequest;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/walkPoint")
@Slf4j
public class WalkPointBallController {
	
	@Autowired
	private WalkService walkService; 

	
	 @PostMapping(value = "/inquireWalkPointBall")  
	 @ResponseBody
	 public ResponseEntity<?> inquireWalkPointBall(@RequestBody StepNumWrapper stepNumWrapper){		 
		 String userUuid = ShiroUtils.getUserUuid();
		 log.info("userUuid={}",userUuid);
		 WalkBallReturn walkBallReturn = walkService.inquireWalkPointBall(userUuid, stepNumWrapper.getStepNum());
		 
		return new ResponseEntity.Builder<WalkBallReturn>()
				.setData(walkBallReturn)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		 
	 }
	 
	 
	 @PostMapping(value = "/takeWalkPointBall")  
	 @ResponseBody
	 public ResponseEntity<?> takeWalkPointBall(@RequestBody WalkBallTokenRequest walkBallTokenRequest) throws ParseException{
		 String userUuid = ShiroUtils.getUserUuid();
		 ErrorCode errorCode = ErrorCode.SUCCESS;
		 //行走能量球查询
		 walkService.inquireWalkPointBall(userUuid, walkBallTokenRequest.getStepNum());
		 EnergyBallTakenResult energyBallTakenResult = walkService.takeWalkPointBall(userUuid, walkBallTokenRequest.getBallId(), walkBallTokenRequest.getStepNum());
		 if(energyBallTakenResult == null)
			 errorCode = ErrorCode.GENERAL_ERROR;
		return new ResponseEntity.Builder<EnergyBallTakenResult>()
				.setData(energyBallTakenResult)
				.setErrorCode(errorCode)
				.build();
		 
	 }

}
