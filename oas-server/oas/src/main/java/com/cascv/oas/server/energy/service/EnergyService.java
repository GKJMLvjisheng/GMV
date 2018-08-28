package com.cascv.oas.server.energy.service;

import com.cascv.oas.server.energy.mapper.EnergySourcePointMapper;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.mapper.UserEnergyMapper;
import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.vo.EnergyBallWithTime;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    private static final Integer STATUS_OF_NEW_BORN_ENERGYBALL = 1;
    private static final Integer SOURCE_OF_CHECKIN = 1;
    /**
     * 查询当日是否签过到，已签到返回false，当日尚未签到返回true
     * @param userId
     * @return
     */
    public Boolean isCheckin(Integer userId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String time = sdf.format(new Date());
        EnergyBallWithTime energyBallWithTime = new EnergyBallWithTime(userId, time);
        List<EnergyBall> energyBalls = energyBallMapper.selectByTimeFuzzyQuery(energyBallWithTime);
        return CollectionUtils.isEmpty(energyBalls) ? true : false;
    }

    public EnergyBall setEnergyBallCheckin(Integer userId) {
        EnergyBall energyBallCheckin = new EnergyBall();
        energyBallCheckin.setUserId(userId);
        energyBallCheckin.setPointSource(SOURCE_OF_CHECKIN);
        energyBallCheckin.setPowerSource(SOURCE_OF_CHECKIN);
        energyBallCheckin.setStatus(STATUS_OF_NEW_BORN_ENERGYBALL);
        EnergyCheckinResult checkinEnergy = this.getCheckinEnergy();
        energyBallCheckin.setPoint(checkinEnergy.getNewEnergyPoint());
        energyBallCheckin.setPower(checkinEnergy.getNewPower());
        Date currentTime = new Date();
        energyBallCheckin.setTimeCreated(currentTime);
        energyBallCheckin.setTimeUpdated(currentTime);
        return energyBallCheckin;
    }

    /**
     * 获取签到的属性：增加积分、算力的数值
     * @return
     */
    public EnergyCheckinResult getCheckinEnergy() {
        BigDecimal point = energySourcePointMapper.queryPointSingle().getPointSingle();
        BigDecimal power = energySourcePowerMapper.queryPowerSingle().getPowerSingle();
        EnergyCheckinResult energyCheckinResult = new EnergyCheckinResult();
        energyCheckinResult.setNewEnergyPoint(point);
        energyCheckinResult.setNewPower(power);
        return energyCheckinResult;
    }
}