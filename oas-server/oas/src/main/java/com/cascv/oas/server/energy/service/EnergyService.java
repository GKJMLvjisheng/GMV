package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergySourcePointMapper;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.mapper.UserEnergyMapper;
import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.vo.EnergyBallWithTime;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import com.cascv.oas.server.utils.ShiroUtils;
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
    private static final Integer SOURCE_CODE_OF_CHECKIN = 1;
    /**
     * 查询当日是否签过到，已签到返回 true，当日尚未签到返回 false
     * @param userId
     * @return
     */
    public Boolean isCheckin(String userId) {
        String today = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
        EnergyBallWithTime energyBallWithTime = new EnergyBallWithTime(userId, today);
        List<EnergyBall> energyBalls = energyBallMapper.selectByTimeFuzzyQuery(energyBallWithTime);
        return CollectionUtils.isEmpty(energyBalls) ? false : true;
    }

    /**
     * 生成签到EnergyBall
     * @param userId
     * @return
     */
    public EnergyBall setEnergyBallCheckin(String userId) {
        EnergyBall energyBallOfCheckin = new EnergyBall();
        energyBallOfCheckin.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        energyBallOfCheckin.setUserUuid(userId);
        energyBallOfCheckin.setPointSource(SOURCE_CODE_OF_CHECKIN);
        energyBallOfCheckin.setPowerSource(SOURCE_CODE_OF_CHECKIN);
        energyBallOfCheckin.setStatus(STATUS_OF_NEW_BORN_ENERGYBALL);

        EnergyCheckinResult checkinEnergy = this.getCheckinEnergy();
        energyBallOfCheckin.setPoint(checkinEnergy.getNewEnergyPoint());
        energyBallOfCheckin.setPower(checkinEnergy.getNewPower());

        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        energyBallOfCheckin.setTimeCreated(now);
        energyBallOfCheckin.setTimeUpdated(now);
        return energyBallOfCheckin;
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