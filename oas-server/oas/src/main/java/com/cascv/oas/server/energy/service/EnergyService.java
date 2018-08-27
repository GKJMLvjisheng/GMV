package com.cascv.oas.server.energy.service;

import com.cascv.oas.server.blockchain.vo.EnergyPointCheckinResult;
import com.cascv.oas.server.energy.mapper.EnergySourcePointMapper;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.mapper.UserEnergyMapper;
import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.vo.EnergyBallWithTime;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnergyService {

    @Autowired
    private EnergyBallMapper energyBallMapper;
    @Autowired
    private UserEnergyMapper userEnergyMapper;
    @Autowired
    private EnergySourcePointMapper energySourcePointMapper;
    @Autowired
    private EnergySourcePowerMapper energySourcePowerMapper;

    /**
     * 查询当日是否已有签到记录
     * @param energyBallWithTime
     * @return
     */
    public EnergyBall getEnergyBallByFuzzyTime(EnergyBallWithTime energyBallWithTime) {
        List<EnergyBall> energyBalls = energyBallMapper.selectByTimeFuzzyQuery(energyBallWithTime);
        return CollectionUtils.isEmpty(energyBalls) ? null : energyBalls.get(0);
    }

    /**
     * 获取签到的属性：增加积分、算力的数值
     * @return
     */
    public EnergyPointCheckinResult getCheckinEnergy() {
        Integer point = energySourcePointMapper.queryPointSingle().getPointSingle();
        Integer power = energySourcePowerMapper.queryPowerSingle().getPowerSingle();
        EnergyPointCheckinResult energyPointCheckinResult = new EnergyPointCheckinResult();
        energyPointCheckinResult.setNewEnergyPoint(point);
        energyPointCheckinResult.setNewPower(power);
        return energyPointCheckinResult;
    }

    /**
     * 获取用户当前的积分、算力值
     * @param userId
     * @return
     */
    public UserEnergy getCurrentEnergyResult(Integer userId) {
        return userEnergyMapper.selectByUserId(userId);
    }

    /**
     * 获得未被获取过能量的能量球列表
     * @return
     */
    public List<EnergyBall> listEnergyBallByStatus() {
        List<EnergyBall> energyBalls = energyBallMapper.selectByStatus();
        return energyBalls;
    }
}