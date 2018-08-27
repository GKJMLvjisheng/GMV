package com.cascv.oas.server.energy.mapper;

import com.cascv.oas.server.blockchain.vo.EnergyPointCheckinResult;
import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.vo.EnergyPointAndPower;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.vo.EnergyBallWithTime;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface EnergyBallMapper {
    /**
     * 根据 id 与时间模糊查询
     * @param energyBallWithTime
     * @return
     */
    List<EnergyBall> selectByTimeFuzzyQuery(EnergyBallWithTime energyBallWithTime);

    /**
     * 查询出所有未被获取过能量的能量球
     * @return
     */
    List<EnergyBall> selectByStatus();



}