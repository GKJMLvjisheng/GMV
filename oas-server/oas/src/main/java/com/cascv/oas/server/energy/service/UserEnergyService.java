package com.cascv.oas.server.energy.service;

import com.cascv.oas.server.energy.mapper.UserEnergyMapper;
import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserEnergyService {
    @Autowired
    private UserEnergyMapper userEnergyMapper;
    @Autowired
    private EnergyService energyService;
    /**
     * 获取用户最新的energy记录
     * @param userId
     * @return
     */
    public UserEnergy getNewestEnergyResult(Integer userId) {
        List<UserEnergy> userEnergies = userEnergyMapper.selectByUserId(userId);
        return CollectionUtils.isEmpty(userEnergies) ? null : userEnergies.get(0);
    }

    /**
     * 用户能量表中插入记录，返回该记录的主键
     * @param userId
     * @param energyBallId
     * @return
     */
    public EnergyCheckinResult saveUserEnergy(Integer userId, Integer energyBallId) {
        UserEnergy userEnergy = new UserEnergy();
        userEnergy.setUserId(userId);
        userEnergy.setEnergyBallId(energyBallId);
        UserEnergy newestEnergyResult = this.getNewestEnergyResult(userId);
        EnergyCheckinResult checkinEnergy = energyService.getCheckinEnergy();
        userEnergy.setBalancePoint(newestEnergyResult.getBalancePoint().add(checkinEnergy.getNewEnergyPoint()));
        userEnergy.setBalancePower(newestEnergyResult.getBalancePower().add(checkinEnergy.getNewPower()));
        Date time = new Date();
        userEnergy.setTimeCreated(time);
        userEnergy.setTimeUpdated(time);
        int success = userEnergyMapper.insertUserEnergy(userEnergy);
        return checkinEnergy;
    }
}
