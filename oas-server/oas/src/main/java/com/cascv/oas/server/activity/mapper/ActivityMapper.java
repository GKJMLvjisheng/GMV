package com.cascv.oas.server.activity.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.activity.model.ActivityModel;
import com.cascv.oas.server.activity.model.ActivityRewardConfig;
import com.cascv.oas.server.activity.model.RewardModel;
import com.cascv.oas.server.activity.wrapper.ActivityRewardUpdate;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;

@Component
public interface ActivityMapper {
	
	Integer insertActivity(ActivityModel activityModel);
	List<ActivityModel> selectAllActivity();
	Integer deleteActivity(@Param("sourceCode") Integer sourceCode);
	
	Integer insertRewardType(RewardModel rewardModel);
	List<RewardModel> selectAllReward();
	Integer deleteReward(@Param("rewardCode") Integer rewardCode);
	
	ActivityRewardConfig selectMaxValueBySourceCodeAndType(@Param("sourceCode") Integer sourceCode, @Param("type") Integer type);
	Integer insertActivityRewardConfig(ActivityRewardConfig activityRewardConfig);
	Integer updateActivityReward(ActivityRewardConfig activityRewardConfig);
	List<ActivityRewardConfig> selectActivityRewardBySourceCode(@Param("sourceCode") Integer sourceCode);
	Integer deleteActivityReward(@Param("sourceCode") Integer sourceCode, @Param("type") Integer type);
	
	Integer insertEnergyPointBall(EnergyBall energyBall);
	Integer insertEnergyPowerBall(EnergyBall energyBall);
	Integer insertPointTradeRecord(EnergyTradeRecord energyTradeRecord);
	Integer insertPowerTradeRecord(EnergyTradeRecord energyTradeRecord);
	
	Integer insertActivityCompletionStatus(ActivityCompletionStatus activityCompletionStatus);
	
	Integer increasePoint(@Param("userUuid") String userUuid, @Param("value") BigDecimal value);
	Integer decreasePoint(@Param("userUuid") String userUuid, @Param("value") BigDecimal value);
	Integer increasePower(@Param("userUuid") String userUuid, @Param("value") BigDecimal value);
	Integer decreasePower(@Param("userUuid") String userUuid, @Param("value") BigDecimal value);

}
