package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import com.cascv.oas.server.energy.vo.EnergyChangeDetail;
import com.cascv.oas.server.energy.vo.EnergyPowerChangeDetail;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public interface EnergyTradeRecordMapper {
    int insertEnergyTradeRecord(EnergyTradeRecord energyTradeRecord);
    BigDecimal sumPoint(@Param("userUuid") String userUuid, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
    BigDecimal sumInPoint(@Param("userUuid") String userUuid, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
    BigDecimal sumOutPoint(@Param("userUuid") String userUuid, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
    List<EnergyTradeRecord> selectTrade(@Param("userUuid") String userUuid, @Param("beginTime") String beginTime, @Param("endTime") String endTime);
    Integer updateStatus(@Param("uuid") String uuid, @Param("newStatus") Integer newStatus);
    Integer countByUserUuid(@Param("userUuid") String userUuid);
    List<EnergyChangeDetail> selectByPage(@Param("userUuid") String userUuid, @Param("offset") Integer offset, @Param("limit") Integer limit);
    List<EnergyChangeDetail> selectByOutPage(@Param("userUuid") String userUuid, @Param("offset") Integer offset, @Param("limit") Integer limit);
    List<EnergyChangeDetail> selectByAllPage(@Param("userUuid") String userUuid, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    List<EnergyPowerChangeDetail> selectPowerByPage(@Param("userUuid") String userUuid, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
}
