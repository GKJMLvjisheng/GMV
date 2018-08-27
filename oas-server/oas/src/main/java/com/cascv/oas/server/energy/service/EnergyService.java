package com.cascv.oas.server.energy.service;

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

    /**
     * 查询当日是否已有签到记录
     * @param energyBallWithTime
     * @return
     */
    public EnergyBall getEnergyBallByFuzzyTime(EnergyBallWithTime energyBallWithTime) {
        List<EnergyBall> energyBalls = energyBallMapper.selectByTimeFuzzyQuery(energyBallWithTime);
        return CollectionUtils.isEmpty(energyBalls) ? null : energyBalls.get(0);
    }

    public int saveEnergyBallOnCheckin() {

        return 0;
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