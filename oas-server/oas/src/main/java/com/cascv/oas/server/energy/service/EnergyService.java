package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergySourcePointMapper;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.mapper.UserEnergyMapper;
import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.dto.EnergyBallWithTime;
import com.cascv.oas.server.energy.model.UserEnergy;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

    private static EnergyBall checkinEnergyBall = new EnergyBall();

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
     * 插入签到EnergyBall
     * @param userUuid
     * @return
     */
    public int saveCheckinEnergyBall(String userUuid) {
        checkinEnergyBall = this.setCheckinEnergyBall(userUuid);
        return energyBallMapper.insertEnergyBall(checkinEnergyBall);
    }

    /**
     * 用户能量表中插入记录
     * @param userId
     * @return
     */
    public EnergyCheckinResult saveUserEnergy(String userId) {
        UserEnergy userEnergy = new UserEnergy();
        userEnergy.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        userEnergy.setUserUuid(userId);
        userEnergy.setEnergyBallUuid(checkinEnergyBall.getUuid());
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        userEnergy.setTimeCreated(now);
        userEnergy.setTimeUpdated(now);

        EnergyCheckinResult checkinEnergy = this.getCheckinEnergy();
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

    /**
     * 根据能量球 id 更新其状态，将状态
     * @param id
     * @return
     */
    public int updateEnergyBallStatusById(String id) {
        return energyBallMapper.updateEnergyBallStatusById(id);
    }

    /**
     * 产生签到EnergyBall
     * @param userUuid
     * @return
     */
    public EnergyBall setCheckinEnergyBall(String userUuid) {
        EnergyBall energyBallOfCheckin = new EnergyBall();
        energyBallOfCheckin.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        energyBallOfCheckin.setUserUuid(userUuid);
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

    /**
     * 获取用户最新的energy记录
     * @param userId
     * @return
     */
    public UserEnergy getNewestEnergyResult(String userId) {
        List<UserEnergy> userEnergies = userEnergyMapper.selectByUserId(userId);
        return CollectionUtils.isEmpty(userEnergies) ? null : userEnergies.get(0);
    }
}