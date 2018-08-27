package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergySourcePoint;

public interface EnergySourcePointMapper {
    int updateByPrimaryKeySelective(EnergySourcePoint record);
    int updateByPrimaryKey(EnergySourcePoint record);
}