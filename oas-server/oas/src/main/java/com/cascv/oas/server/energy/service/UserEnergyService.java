package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.UserEnergyMapper;
import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import com.cascv.oas.server.utils.ShiroUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public UserEnergy getNewestEnergyResult(String userId) {
        List<UserEnergy> userEnergies = userEnergyMapper.selectByUserId(userId);
        return CollectionUtils.isEmpty(userEnergies) ? null : userEnergies.get(0);
    }

    /**
     * 用户能量表中插入记录
     * @param userId
     * @param energyBallId
     * @return
     */
    public EnergyCheckinResult saveUserEnergy(String userId, String energyBallId) {
        UserEnergy userEnergy = new UserEnergy();
        userEnergy.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        userEnergy.setUserUuid(userId);
        userEnergy.setEnergyBallUuid(energyBallId);
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        userEnergy.setTimeCreated(now);
        userEnergy.setTimeUpdated(now);

        EnergyCheckinResult checkinEnergy = energyService.getCheckinEnergy();
        UserEnergy newestEnergyResult = this.getNewestEnergyResult(userId);
        if(newestEnergyResult == null) {
            userEnergy.setBalancePoint(checkinEnergy.getNewEnergyPoint().add(BigDecimal.ZERO));
            userEnergy.setBalancePower(checkinEnergy.getNewPower().add(BigDecimal.ZERO));
        }else {
            userEnergy.setBalancePoint(newestEnergyResult.getBalancePoint().add(checkinEnergy.getNewEnergyPoint()));
            userEnergy.setBalancePower(newestEnergyResult.getBalancePower().add(checkinEnergy.getNewPower()));
        }
        userEnergyMapper.insertUserEnergy(userEnergy);
        return checkinEnergy;
    }
}
