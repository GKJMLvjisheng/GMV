package com.cascv.oas.server.activity.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.activity.model.ActivityModel;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import com.cascv.oas.server.energy.model.EnergyWallet;

@Component
public interface ActivityMapper {
	
	Integer insertActivity(ActivityModel activityModel);
	List<ActivityModel> selectAllActivity();
	Integer updateActivity(ActivityModel activityModel);
	ActivityModel selectActivityBySourceCode(@Param("sourceCode")Integer sourceCode);
	
	Integer insertEnergyBall(EnergyBall energyBall);
	Integer insertEnergyTradeRecord(EnergyTradeRecord energyTradeRecord);
	Integer insertActivityCompletionStatus(ActivityCompletionStatus activityCompletionStatus);
	
	Integer increasePoint(@Param("userUuid")  String userUuid, @Param("value") BigDecimal value);
	Integer decreasePoint(@Param("userUuid")  String userUuid, @Param("value") BigDecimal value);
	Integer increasePower(@Param("userUuid")  String userUuid, @Param("value") BigDecimal value);
	Integer decreasePower(@Param("userUuid")  String userUuid, @Param("value") BigDecimal value);

}
