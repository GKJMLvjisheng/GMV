package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.activity.model.ActivityRewardConfig;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface EnergySourcePointMapper {

	ActivityRewardConfig queryBySourceCode(@Param("sourceCode") Integer sourceCode);

    BigDecimal queryPointSingle(@Param("sourceCode")Integer sourceCode, @Param("rewardCode")Integer rewardCode);
}