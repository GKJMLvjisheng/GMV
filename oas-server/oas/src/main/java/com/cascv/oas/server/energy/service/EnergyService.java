package com.cascv.oas.server.energy.service;

import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.vo.EnergyBallWithTimeVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnergyService {

    @Autowired
    private EnergyBallMapper energyBallMapper;

    /**
     *
     * @param energyBallWithTimeVo
     * @return
     */
    public EnergyBall getEnergyBallByFuzzyTime(EnergyBallWithTimeVo energyBallWithTimeVo) {
        // 模糊查询，通过vo对象energyBallWithTime
        List<EnergyBall> energyBalls = energyBallMapper.selectByTimeFuzzyQuery(energyBallWithTimeVo);
        return CollectionUtils.isEmpty(energyBalls) ? null : energyBalls.get(0);
    }
}