package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.energy.model.UserEnergy;
import org.springframework.stereotype.Component;

@Component
public interface UserEnergyMapper {
    /**
     * 查询用户当前积分、算力值
     * @param userId
     * @return
     */
    UserEnergy selectByUserId(Integer userId);
}