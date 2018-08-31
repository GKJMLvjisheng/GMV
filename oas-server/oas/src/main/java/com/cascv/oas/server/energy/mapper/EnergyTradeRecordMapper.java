package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EnergyTradeRecordMapper {
    /**
     * 根据userId 查询userEnergy列表
     * @param userId
     * @return
     */
    List<EnergyTradeRecord> selectByUserId(String userId);

    /**
     *
     * @param energyTradeRecord
     * @return
     */
    int insertUserEnergy(EnergyTradeRecord energyTradeRecord);
}