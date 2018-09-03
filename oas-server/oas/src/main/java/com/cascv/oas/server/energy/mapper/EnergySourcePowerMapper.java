package com.cascv.oas.server.energy.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface EnergySourcePowerMapper {
    BigDecimal queryPowerSingle(@Param("sourceCode")Integer sourceCode);
}