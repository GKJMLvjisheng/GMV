package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergySourcePower;
import org.springframework.stereotype.Component;

@Component
public interface EnergySourcePowerMapper {
    EnergySourcePower queryPowerSingle();
}