package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyTradeRecord;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface EnergyTradeRecordMapper {
    int insertEnergyTradeRecord(EnergyTradeRecord energyTradeRecord);
    BigDecimal sumPoint(@Param("userUuid") String userUuid, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
    List<EnergyTradeRecord> selectTrade(@Param("userUuid") String userUuid, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
    Integer updateStatus(@Param("uuid") String uuid, @Param("newStatus") Integer newStatus);
    List<EnergyTradeRecord> selectByPage(@Param("userUuid") String userUuid, @Param("offset") Integer offset, @Param("limit") Integer limit);
}
