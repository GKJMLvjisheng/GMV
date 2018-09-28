package com.cascv.oas.server.activity.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.activity.model.ActivityModel;
import com.cascv.oas.server.activity.model.ActivityRewardConfig;
import com.cascv.oas.server.activity.model.EnergyPointBall;
import com.cascv.oas.server.activity.model.EnergyPowerBall;
import com.cascv.oas.server.activity.model.PointTradeRecord;
import com.cascv.oas.server.activity.model.PowerTradeRecord;
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
	RewardModel selectRewardByRewardCode(@Param("rewardCode") Integer rewardCode);
	Integer deleteReward(@Param("rewardCode") Integer rewardCode);
	
	ActivityRewardConfig selectMaxValueBySourceCodeAndrewardCode(@Param("sourceCode") Integer sourceCode, @Param("rewardCode") Integer rewardCode);
	Integer insertActivityRewardConfig(ActivityRewardConfig activityRewardConfig);
	Integer updateActivityReward(ActivityRewardConfig activityRewardConfig);
	List<ActivityRewardConfig> selectActivityRewardBySourceCode(@Param("sourceCode") Integer sourceCode);
	Integer deleteActivityReward(@Param("sourceCode") Integer sourceCode, @Param("rewardCode") Integer rewardCode);
	
	Integer insertEnergyPointBall(EnergyPointBall energyPointBall);
	Integer insertEnergyPowerBall(EnergyPowerBall energyPowerBall);
	Integer insertPointTradeRecord(PointTradeRecord pointTradeRecord);
	Integer insertPowerTradeRecord(PowerTradeRecord powerTradeRecord);
	
	Integer insertActivityCompletionStatus(ActivityCompletionStatus activityCompletionStatus);
	
	Integer increasePoint(@Param("userUuid") String userUuid, @Param("value") BigDecimal value);
	Integer decreasePoint(@Param("userUuid") String userUuid, @Param("value") BigDecimal value);
	Integer increasePower(@Param("userUuid") String userUuid, @Param("value") BigDecimal value);
	Integer decreasePower(@Param("userUuid") String userUuid, @Param("value") BigDecimal value);

}
