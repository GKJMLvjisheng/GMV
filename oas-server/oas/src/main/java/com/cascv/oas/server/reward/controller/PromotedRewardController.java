package com.cascv.oas.server.reward.controller;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.reward.mapper.PromotedRewardModelMapper;
import com.cascv.oas.server.reward.model.PromotedRewardModel;
import com.cascv.oas.server.reward.service.PromotedRewardService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")
public class PromotedRewardController {
	@Autowired
	private PromotedRewardService promotedRewardService;
	@Autowired 
	private PromotedRewardModelMapper promotedRewardModelMapper;
	
	@PostMapping(value="/inqureAllPromotedRewardConfig")
	@ResponseBody
	public ResponseEntity<?> inqureAllPromotedRewardConfig(){
		List<PromotedRewardModel> promotedRewardModelList = promotedRewardService.selectAllPromotedRewardConfig();
		return new ResponseEntity.Builder<List<PromotedRewardModel>>()
					.setData(promotedRewardModelList)
					.setErrorCode(ErrorCode.SUCCESS)
					.build();
	}
	
	@PostMapping(value="/updatePromotedRewardConfig")
	@ResponseBody
	public ResponseEntity<?> updatePromotedRewardConfig(@RequestBody PromotedRewardModel promotedRewardModelInfo){
		PromotedRewardModel promotedRewardModel = new PromotedRewardModel();
		String created=DateUtils.getTime();
		log.info("created:{}",created);
		
		promotedRewardModel.setPromotedId(promotedRewardModelInfo.getPromotedId());
		promotedRewardModel.setRewardName(promotedRewardModelInfo.getRewardName());
		BigDecimal frozenRatio=promotedRewardModelInfo.getFrozenRatio();
		BigDecimal rewardRatio=promotedRewardModelInfo.getRewardRatio();
		promotedRewardModel.setFrozenRatio(frozenRatio);
		BigDecimal a=new BigDecimal(1);
		if(rewardRatio.compareTo(BigDecimal.ZERO)==1 && rewardRatio.compareTo(a)==-1) {
		promotedRewardModel.setRewardRatio(rewardRatio);
		}else {
			return new ResponseEntity.Builder<Integer>()
					.setData(1)
					.setErrorCode(ErrorCode.GENERAL_ERROR)
					.build();
		}
		promotedRewardModel.setMaxPromotedGrade(promotedRewardModelInfo.getMaxPromotedGrade());
		promotedRewardModel.setCreated(created);
		
		promotedRewardModelMapper.updatePromotedReward(promotedRewardModel);
		
		return new ResponseEntity.Builder<PromotedRewardModel>()
				.setData(promotedRewardModel)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	@PostMapping(value="/AAAAtest")
	@ResponseBody
	public ResponseEntity<?> AAAAtest(){
		String str="大c中z国";
		String str1="大中国zxc";
		String str2="wddrftf";
		Integer length=str.length();
		Integer length1=str1.length();
		Integer length2=str2.length();
		str=str.substring(length-3, length);
		str1=str1.substring(length1-3, length1);
		str2=str2.substring(length2-3, length2);
		log.info("str:{}",str);
		log.info("str1:{}",str1);
		log.info("str2:{}",str2);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
}