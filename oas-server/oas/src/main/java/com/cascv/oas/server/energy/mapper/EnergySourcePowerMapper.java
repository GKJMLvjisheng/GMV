package com.cascv.oas.server.energy.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.vo.ActivityResult;
import com.cascv.oas.server.energy.vo.ActivityResultList;
import java.math.BigDecimal;
import java.util.List;

@Component
public interface EnergySourcePowerMapper {
    BigDecimal queryPowerSingle(@Param("sourceCode")Integer sourceCode);
    List<ActivityResult> selectByUserUuid(@Param("userUuid")String userUuid);
    ActivityResult selectStatusByUserUuid(@Param("userUuid")String userUuid);
    Integer insertActivity(ActivityCompletionStatus activityCompletionStatus);
    Integer update(ActivityCompletionStatus activityCompletionStatus);
}