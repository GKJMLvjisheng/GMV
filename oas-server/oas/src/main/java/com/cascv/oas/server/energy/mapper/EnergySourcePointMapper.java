package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.EnergySourcePoint;
import org.springframework.stereotype.Component;

@Component
public interface EnergySourcePointMapper {
    EnergySourcePoint queryPointSingle();
}