package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EnergyTradeRecordMapper {
    int insertEnergyTradeRecord(EnergyTradeRecord energyTradeRecord);
}