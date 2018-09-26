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
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.ActivityModel;
import com.cascv.oas.server.activity.service.ActivityService;
import com.cascv.oas.server.activity.wrapper.ActivityGetReward;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/activityConfig")
@Slf4j
public class ActivityController {
	
	@Autowired
	private ActivityService activityService;
	@Autowired
	private ActivityMapper activityMapper;
	
	@PostMapping(value = "/addActivity")
    @ResponseBody
    public ResponseEntity<?> addActivity(@RequestBody ActivityModel activityModelList){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		ActivityModel activityModel = new ActivityModel();
		activityModel.setSourceCode(activityModelList.getSourceCode());
		activityModel.setSourceName(activityModelList.getSourceName());
		activityModel.setType(activityModelList.getType());
		activityModel.setPointSingle(activityModelList.getPointSingle());
		activityModel.setPowerSingle(activityModelList.getPowerSingle());
		activityModel.setPointIncreaseSpeed(activityModelList.getPointIncreaseSpeed());
		activityModel.setPowerIncreaseSpeed(activityModelList.getPowerIncreaseSpeed());
		activityModel.setPointIncreaseSpeedUnit(activityModelList.getPointIncreaseSpeedUnit());
		activityModel.setPowerIncreaseSpeedUnit(activityModelList.getPowerIncreaseSpeedUnit());
		activityModel.setPointCapacityEachBall(activityModelList.getPointCapacityEachBall());
		activityModel.setPowerCapacityEachBall(activityModelList.getPowerCapacityEachBall());
		activityModel.setCreated(now);
		activityModel.setUpdated(now);
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
	
	
	@PostMapping(value = "/updateActivity")
    @ResponseBody
    public ResponseEntity<?> updateActivity(@RequestBody ActivityModel activityModelList){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		log.info("sourceCode",activityModelList.getSourceCode());
		ActivityModel activityModel = new ActivityModel();
		activityModel.setSourceCode(activityModelList.getSourceCode());
		activityModel.setSourceName(activityModelList.getSourceName());
		activityModel.setType(activityModelList.getType());
		activityModel.setPointSingle(activityModelList.getPointSingle());
		activityModel.setPowerSingle(activityModelList.getPowerSingle());
		activityModel.setPointIncreaseSpeed(activityModelList.getPointIncreaseSpeed());
		activityModel.setPowerIncreaseSpeed(activityModelList.getPowerIncreaseSpeed());
		activityModel.setPointIncreaseSpeedUnit(activityModelList.getPointIncreaseSpeedUnit());
		activityModel.setPowerIncreaseSpeedUnit(activityModelList.getPowerIncreaseSpeedUnit());
		activityModel.setPointCapacityEachBall(activityModelList.getPointCapacityEachBall());
		activityModel.setPowerCapacityEachBall(activityModelList.getPowerCapacityEachBall());
		activityModel.setCreated(activityModelList.getCreated());
		activityModel.setUpdated(now);
		activityMapper.updateActivity(activityModel);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/getReward")
    @ResponseBody
    public ResponseEntity<?> getReward(@RequestBody ActivityGetReward activityGetReward ){
		String userUuid = ShiroUtils.getUserUuid();
		Integer sourceCode = activityGetReward.getSourceCode();
		activityService.addEnergyBall(userUuid, sourceCode);
		activityService.addEnergyTradeRecord(userUuid, sourceCode);
		activityService.updateEnergyWallet(userUuid, sourceCode);
		activityService.addActivityCompletionStatus(userUuid, sourceCode);
		return new ResponseEntity.Builder<>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}

}
