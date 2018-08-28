package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.UserEnergy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserEnergyMapper {
    /**
     * 根据userId 查询userEnergy列表
     * @param userId
     * @return
     */
    List<UserEnergy> selectByUserId(String userId);

    int insertUserEnergy(UserEnergy userEnergy);
}