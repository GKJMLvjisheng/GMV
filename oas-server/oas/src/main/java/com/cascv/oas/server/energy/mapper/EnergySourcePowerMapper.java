package com.cascv.oas.server.energy.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import java.math.BigDecimal;

@Component
public interface EnergySourcePowerMapper {
    BigDecimal queryPowerSingle(@Param("sourceCode")Integer sourceCode);   
    ActivityCompletionStatus selectByUserUuid(@Param("userUuid")String userUuid);
    Integer insertActivity(ActivityCompletionStatus activityCompletionStatus);
}