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
import com.cascv.oas.server.activity.wrapper.RewardConfigResult;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;

@Component
public interface ActivityMapper {
	
	Integer insertActivity(ActivityModel activityModel);
	List<ActivityModel> selectAllActivity();
	Integer deleteActivity(@Param("sourceUuid") String sourceUuid);
	ActivityModel selectActivityBySourceUuid(@Param("sourceUuid") String sourceUuid);
	ActivityModel selectActivityBySourceCode(@Param("sourceCode") Integer sourceCode);
	Integer updateSourceCode(@Param("sourceUuid") String sourceUuid,
			@Param("sourceCode") Integer sourceCode);
	
	Integer insertRewardType(RewardModel rewardModel);
	List<RewardModel> selectAllReward();
	RewardModel selectRewardByCode(@Param("rewardCode") Integer rewardCode);
	RewardModel selectRewardByRewardCode(@Param("rewardUuid") String rewardUuid);
	Integer deleteReward(@Param("rewardUuid") String rewardUuid);
	Integer updateRewardCode(@Param("rewardUuid") String rewardUuid,
			@Param("rewardCode") Integer rewardCode);
	
	ActivityRewardConfig selectBaseValueBySourceCodeAndRewardCode(@Param("sourceUuid") String sourceUuid, @Param("rewardUuid") String rewardUuid);
	Integer insertActivityRewardConfig(ActivityRewardConfig activityRewardConfig);
	Integer updateActivityReward(ActivityRewardConfig activityRewardConfig);
	List<RewardConfigResult> selectActivityRewardBySourceCode(@Param("sourceUuid") String sourceUuid);
	Integer deleteActivityReward(@Param("sourceUuid") String sourceUuid, @Param("rewardUuid") String rewardUuid);
	
	Integer insertEnergyPointBall(EnergyPointBall energyPointBall);
	Integer deleteEnergyPointBall(@Param("uuid") String uuid);
	Integer insertEnergyPowerBall(EnergyPowerBall energyPowerBall);
	Integer insertPointTradeRecord(PointTradeRecord pointTradeRecord);
	Integer insertPowerTradeRecord(PowerTradeRecord powerTradeRecord);
	
	EnergyPointBall selectByUuid(@Param("uuid") String uuid);
	List<EnergyPointBall> selectAllByUserUuid(@Param("userUuid") String userUuid,
            @Param("status") Integer status,
            @Param("timeGap") Integer timeGap,
            @Param("updated") String updated);
	List<EnergyPointBall> selectByPointSourceCode(@Param("userUuid") String userUuid,
            @Param("sourceUuid") String sourceUuid,
            @Param("status") Integer status);
	EnergyPointBall selectLatestOneByPointSourceCode(@Param("userUuid") String userUuid,
            @Param("sourceUuid") String sourceUuid,
            @Param("status") Integer status);
	
	Integer updatePointStatusByUuid(@Param("uuid") String uuid,
            @Param("status") Integer status,
            @Param("updated") String updated);
	Integer updatePowerStatusByUuid(@Param("uuid") String uuid,
            @Param("status") Integer status,
            @Param("updated") String updated);
	
	Integer insertActivityCompletionStatus(ActivityCompletionStatus activityCompletionStatus);
	
	Integer increasePoint(@Param("userUuid") String userUuid, @Param("value") BigDecimal value, @Param("updated") String updated);
	Integer decreasePoint(@Param("userUuid") String userUuid, @Param("value") BigDecimal value, @Param("updated") String updated);
	Integer increasePower(@Param("userUuid") String userUuid, @Param("value") BigDecimal value, @Param("updated") String updated);
	Integer decreasePower(@Param("userUuid") String userUuid, @Param("value") BigDecimal value, @Param("updated") String updated);
	
    
	RewardConfigResult inquireACSByUserUuidAndSouceCode(@Param("sourceUuid") String sourceUuid,@Param("userUuid") String userUuid);
}
