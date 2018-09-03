package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergySourcePoint;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface EnergySourcePointMapper {

    EnergySourcePoint queryBySourceCode(@Param("sourceCode") Integer sourceCode);

    BigDecimal queryPointSingle(@Param("sourceCode")Integer sourceCode);
}