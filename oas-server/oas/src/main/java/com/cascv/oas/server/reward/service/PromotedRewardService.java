package com.cascv.oas.server.reward.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.reward.mapper.PromotedRewardModelMapper;
import com.cascv.oas.server.reward.model.PromotedRewardModel;
import com.cascv.oas.server.timezone.service.TimeZoneService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PromotedRewardService {
	@Autowired 
	private PromotedRewardModelMapper promotedRewardModelMapper;
	@Autowired 
	private TimeZoneService timeZoneService;
	
	public List<PromotedRewardModel> selectAllPromotedRewardConfig(){
		List<PromotedRewardModel> promotedRewardModelList = promotedRewardModelMapper.selectAllPromotedRewards();

			for(PromotedRewardModel promotedRewardModel : promotedRewardModelList)
			{
				String srcFormater="yyyy-MM-dd HH:mm:ss";
				String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, promotedRewardModel.getCreated(), dstFormater, dstTimeZoneId);
				promotedRewardModel.setCreated(created);
				log.info("newCreated={}",created);
			}
		
		return promotedRewardModelList;
	}
}