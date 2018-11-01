package com.cascv.oas.server.activity.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.ActivityModel;
import com.cascv.oas.server.activity.model.ActivityRewardConfig;
import com.cascv.oas.server.activity.model.RewardModel;
import com.cascv.oas.server.activity.service.ActivityService;
import com.cascv.oas.server.activity.wrapper.ActivityDelete;
import com.cascv.oas.server.activity.wrapper.ActivityGetReward;
import com.cascv.oas.server.activity.wrapper.ActivityRequest;
import com.cascv.oas.server.activity.wrapper.ActivityRewardAdd;
import com.cascv.oas.server.activity.wrapper.ActivityRewardUpdate;
import com.cascv.oas.server.activity.wrapper.RewardCode;
import com.cascv.oas.server.activity.wrapper.RewardConfigResult;
import com.cascv.oas.server.activity.wrapper.RewardRequest;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.timezone.service.TimeZoneService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/activityConfig")
@Slf4j

public class ActivityController {
	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityMapper activityMapper;
	@Autowired 
	private TimeZoneService timeZoneService;
	@PostMapping(value = "/addActivity")
    @ResponseBody
    public ResponseEntity<?> addActivity(@RequestBody ActivityRequest activityRequest){
//		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		String now = DateUtils.getYearMonth();
		ActivityModel activityModel = new ActivityModel();
		Integer sourceCode = activityMapper.selectAllActivity().size() + 1;
		activityModel.setSourceUuid(activityRequest.getSourceUuid());
		activityModel.setSourceCode(sourceCode);
		activityModel.setSourceName(activityRequest.getSourceName());
		activityModel.setType(activityRequest.getType());
		activityModel.setCreated(now);
		activityMapper.insertActivity(activityModel);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/selectAllActivity")
    @ResponseBody
    public ResponseEntity<?> selectAllActivity(){
		Map<String,Object> info=new HashMap<>();
		List<ActivityModel> activityList = activityMapper.selectAllActivity();
		if(activityList.size() > 0)
			info.put("activityList", activityList);
		else
			log.info("no message");
		return new ResponseEntity.Builder<>()
				.setData(activityList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/deleteActivity")
    @ResponseBody
    public ResponseEntity<?> deleteActivity(@RequestBody ActivityDelete activityDelete){
		String sourceUuid = activityDelete.getSourceUuid();
		log.info("sourceUuid={}",sourceUuid);
		Integer sourceCode = activityMapper.selectActivityBySourceUuid(activityDelete.getSourceUuid()).getSourceCode();
		log.info("sourceCode={}", sourceCode);
		Integer count = activityMapper.selectAllActivity().size();
		activityMapper.deleteActivity(sourceUuid);
		for(; sourceCode < count; sourceCode++) {
			Integer newSourceCode = sourceCode +1;
			String newUuid = activityMapper.selectActivityBySourceCode(newSourceCode).getSourceUuid();
			activityMapper.updateSourceCode(newUuid, sourceCode);
		}
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/addRewardType")
    @ResponseBody
    public ResponseEntity<?> addRewardType(@RequestBody RewardRequest rewardRequest){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		RewardModel rewardModel = new RewardModel();
		Integer rewardCode = activityMapper.selectAllReward().size() + 1;
		rewardModel.setRewardUuid(rewardRequest.getRewardUuid());
		rewardModel.setRewardCode(rewardCode);
		rewardModel.setRewardName(rewardRequest.getRewardName());
		rewardModel.setRewardDescription(rewardRequest.getRewardDescription());
		rewardModel.setCreated(now);
		activityMapper.insertRewardType(rewardModel);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	
	@PostMapping(value = "/selectAllReward")
    @ResponseBody
    public ResponseEntity<?> selectAllReward(){
		Map<String,Object> info=new HashMap<>();
		List<RewardModel> rewardList = activityMapper.selectAllReward();
		for(RewardModel rewardModel : rewardList)
		{
			String srcFormater="yyyy-MM-dd HH:mm:ss";
			String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String created=DateUtils.string2Timezone(srcFormater, rewardModel.getCreated(), dstFormater, dstTimeZoneId);
			rewardModel.setCreated(created);
			log.info("newCreated={}",created);
		}
		if(rewardList.size() > 0)
			info.put("rewardList", rewardList);
		else
			log.info("no message");
		return new ResponseEntity.Builder<List<RewardModel>>()
				.setData(rewardList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/deleteReward")
    @ResponseBody
    public ResponseEntity<?> deleteReward(@RequestBody RewardCode rewardDelete){
		String rewardUuid = rewardDelete.getRewardUuid();
		log.info("sourceCode={}",rewardUuid);
		Integer rewardCode = activityMapper.selectRewardByRewardCode(rewardUuid).getRewardCode();
		Integer count = activityMapper.selectAllReward().size();
		activityMapper.deleteReward(rewardUuid);
		for(; rewardCode < count; rewardCode++) {
			Integer newRewardCode = rewardCode + 1;
			String newUuid = activityMapper.selectRewardByCode(newRewardCode).getRewardUuid();
			activityMapper.updateRewardCode(newUuid, rewardCode);
		}
		
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/selectRewardByRewardCode")
    @ResponseBody
    public ResponseEntity<?> selectRewardByRewardCode(@RequestBody RewardCode rewardCode){
		RewardModel rewardModel = activityMapper.selectRewardByRewardCode(rewardCode.getRewardUuid());
		return new ResponseEntity.Builder<RewardModel>()
				.setData(rewardModel)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value="/inquireRewardByRewardCode")
	@ResponseBody
	public ResponseEntity<?> inquireRewardByRewardCode(@RequestBody ActivityGetReward activityGetReward){
		String sourceUuid = activityGetReward.getSourceUuid();
		String rewardUuid = activityGetReward.getRewardUuid();
		if(activityService.inquireRewardByRewardCode(sourceUuid, rewardUuid) == null)
			return new ResponseEntity.Builder<Integer>()
			        .setData(0)
			        .setErrorCode(ErrorCode.SUCCESS).build();
		else
			return new ResponseEntity.Builder<Integer>()
			        .setData(0)
			        .setErrorCode(ErrorCode.REWARD_ALREADY_EXIST).build();
		
	}	
	
	@PostMapping(value = "/activityRewardConfig")
    @ResponseBody
    public ResponseEntity<?> activityRewardConfig(@RequestBody ActivityRewardAdd activityRewardAdd){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		ActivityRewardConfig activityRewardConfig = new ActivityRewardConfig();
		activityRewardConfig.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ACTIVITY_REWARD_CONFIG));
		activityRewardConfig.setSourceUuid(activityRewardAdd.getSourceUuid());
		activityRewardConfig.setRewardUuid(activityRewardAdd.getRewardUuid());
		activityRewardConfig.setBaseValue(activityRewardAdd.getBaseValue());
		activityRewardConfig.setIncreaseSpeed(activityRewardAdd.getIncreaseSpeed());
		activityRewardConfig.setIncreaseSpeedUnit(activityRewardAdd.getIncreaseSpeedUnit());
		activityRewardConfig.setMaxValue(activityRewardAdd.getMaxValue());
		activityRewardConfig.setPeriod(activityRewardAdd.getPeriod());
		activityRewardConfig.setCreated(now);
		activityRewardConfig.setUpdated(now);
		activityMapper.insertActivityRewardConfig(activityRewardConfig);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	
	@PostMapping(value = "/selectAllActivityReward")
    @ResponseBody
    public ResponseEntity<?> selectAllActivityReward(@RequestBody ActivityDelete activityDelete){
		String sourceUuid = activityDelete.getSourceUuid();
		Map<String,Object> info=new HashMap<>();
		List<RewardConfigResult> activityRewardList = activityMapper.selectActivityRewardBySourceCode(sourceUuid);
		if(activityRewardList.size() > 0)
			info.put("activityList", activityRewardList);
		else
			log.info("no message");
		return new ResponseEntity.Builder<>()
				.setData(activityRewardList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	
	@PostMapping(value = "/updateActivityRewardConfig")
    @ResponseBody
    public ResponseEntity<?> updateActivityRewardConfig(@RequestBody ActivityRewardUpdate activityRewardList){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		ActivityRewardConfig activityRewardConfig = new ActivityRewardConfig();
		activityRewardConfig.setSourceUuid(activityRewardList.getSourceUuid());
		activityRewardConfig.setRewardUuid(activityRewardList.getRewardUuid());
		activityRewardConfig.setBaseValue(activityRewardList.getBaseValue());
		activityRewardConfig.setIncreaseSpeed(activityRewardList.getIncreaseSpeed());
		activityRewardConfig.setIncreaseSpeedUnit(activityRewardList.getIncreaseSpeedUnit());
		activityRewardConfig.setMaxValue(activityRewardList.getMaxValue());
		activityRewardConfig.setPeriod(activityRewardList.getPeriod());
		activityRewardConfig.setUpdated(now);
		activityMapper.updateActivityReward(activityRewardConfig);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	
	@PostMapping(value = "/deleteActivityReward")
    @ResponseBody
    public ResponseEntity<?> deleteActivityReward(@RequestBody ActivityGetReward activityGetReward){
		String sourceUuid = activityGetReward.getSourceUuid();
		String rewardUuid = activityGetReward.getRewardUuid();
		log.info("sourceCode={}",sourceUuid);
		log.info("type={}", rewardUuid);
		activityMapper.deleteActivityReward(sourceUuid, rewardUuid);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}

}
