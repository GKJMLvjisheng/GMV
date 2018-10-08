package com.cascv.oas.server.energy.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.vo.ActivityResult;
import java.math.BigDecimal;
import java.util.List;

@Component
public interface EnergySourcePowerMapper {
    BigDecimal queryPowerSingle(@Param("sourceCode")Integer sourceCode, @Param("rewardCode")Integer rewardCode);
    List<ActivityResult> selectByUserUuid(@Param("userUuid")String userUuid);
    ActivityResult selectStatusByUserUuid(@Param("userUuid")String userUuid);
    
    
    Integer insertActivity(ActivityCompletionStatus activityCompletionStatus);
    Integer updateStatus(ActivityCompletionStatus activityCompletionStatus);
    ActivityCompletionStatus selectACSByUserUuid(@Param("userUuid")String userUuid,@Param("sourceCode")Integer sourceCode);
}