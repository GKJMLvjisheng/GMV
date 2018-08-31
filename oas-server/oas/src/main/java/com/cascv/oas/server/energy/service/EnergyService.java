package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergySourcePointMapper;
import com.cascv.oas.server.energy.mapper.EnergySourcePowerMapper;
import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.energy.mapper.EnergyTradeRecordMapper;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import com.cascv.oas.server.energy.vo.EnergyBallWrapper;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.lang.model.type.ErrorType;
import java.math.BigDecimal;
import java.util.List;

@Service
public class EnergyService {

    @Autowired
    private EnergyBallMapper energyBallMapper;
    @Autowired
    private EnergyTradeRecordMapper energyTradeRecordMapper;
    @Autowired
    private EnergySourcePointMapper energySourcePointMapper;
    @Autowired
    private EnergySourcePowerMapper energySourcePowerMapper;

    private EnergyBall checkinEnergyBall = new EnergyBall();

    private static final Integer STATUS_OF_NEW_BORN_ENERGYBALL = 1; // 新生能量球状态值为1；
    private static final Integer STATUS_OF_DIE_ENERGYBALL = 0;      // 被获取过能量的能量球已死，状态值为0；
    private static final Integer SOURCE_CODE_OF_CHECKIN = 1;        // 能量球来源：签到为1
    private static final Integer SOURCE_CODE_OF_MINING = 2;         // 能量球来源：挖矿为2
    private static final Integer MAX_COUNT_OF_MINING_ENERGYBALL = 16;

    /**
     * 查询当日是否签过到，已签到返回 true，当日尚未签到返回 false
     * @param userUuid
     * @return
     */
    public Boolean isCheckin(String userUuid) {
        String today = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
        EnergyBall energyBall = new EnergyBall();
        energyBall.setUserUuid(userUuid);
        energyBall.setTimeCreated(today);
        energyBall.setStatus(STATUS_OF_NEW_BORN_ENERGYBALL);
        List<EnergyBall> energyBalls = energyBallMapper.selectByTimeFuzzyQuery(energyBall);
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
        EnergyTradeRecord energyTradeRecord = new EnergyTradeRecord();
        energyTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        energyTradeRecord.setUserUuid(userId);
        energyTradeRecord.setEnergyBallUuid(checkinEnergyBall.getUuid());
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        energyTradeRecord.setTimeCreated(now);
        energyTradeRecord.setTimeUpdated(now);

        EnergyCheckinResult checkinEnergy = this.getCheckinEnergy();
        EnergyTradeRecord newestEnergyResult = this.getNewestEnergyResult(userId);
        if(newestEnergyResult == null) {
            energyTradeRecord.setPointChange(checkinEnergy.getNewEnergyPoint().add(BigDecimal.ZERO));
            energyTradeRecord.setPowerChange(checkinEnergy.getNewPower().add(BigDecimal.ZERO));
        }else {
            energyTradeRecord.setPointChange(newestEnergyResult.getPointChange().add(checkinEnergy.getNewEnergyPoint()));
            energyTradeRecord.setPowerChange(newestEnergyResult.getPowerChange().add(checkinEnergy.getNewPower()));
        }
        energyTradeRecordMapper.insertUserEnergy(energyTradeRecord);
        return checkinEnergy;
    }

    /**
     * 根据能量球uuid 更新其状态，将状态
     * @return
     */
    public int updateEnergyBallStatusById() {
        checkinEnergyBall.setStatus(STATUS_OF_DIE_ENERGYBALL);
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        checkinEnergyBall.setTimeUpdated(now);
        return energyBallMapper.updateEnergyBallStatusById(checkinEnergyBall);
    }

    /**
     * 查询挖矿类型的所有能量球
     * @return
     */
    public List<EnergyBallWrapper> listEnergyBall(String userUuid) {
        // 如果此用户该类型能量球还没有，则生成
        this.miningEnergyBallGenerate(userUuid);
        // 查询所有挖矿能量球信息
        EnergyBall energyBall = new EnergyBall();
        energyBall.setPointSource(SOURCE_CODE_OF_MINING);
        energyBall.setUserUuid(userUuid);
        return energyBallMapper.selectCheckinEnergyBalls(energyBall);
    }

    // 以下为非业务方法
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
    public EnergyTradeRecord getNewestEnergyResult(String userId) {
        List<EnergyTradeRecord> energyTradeRecords = energyTradeRecordMapper.selectByUserId(userId);
        return CollectionUtils.isEmpty(energyTradeRecords) ? null : energyTradeRecords.get(0);
    }

    /**
     * 产生不足数的能量球原球
     */
    public void miningEnergyBallGenerate(String userUuid) {
        // 查询该用户是否产生挖矿能量球
        EnergyBall energyBall = new EnergyBall();
        energyBall.setUserUuid(userUuid);
        energyBall.setPointSource(SOURCE_CODE_OF_MINING);
        List<EnergyBall> energyBalls = energyBallMapper.selectByPointSourceCode(energyBall);
        // 能量球数不足，则产生至需要数目
        if (energyBalls == null || energyBalls.size() < MAX_COUNT_OF_MINING_ENERGYBALL) {
            int countOfGenerate = MAX_COUNT_OF_MINING_ENERGYBALL - energyBalls.size();
            for (int i = 0; i < countOfGenerate; i++) {
                String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
                energyBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
                energyBall.setUserUuid(userUuid);
                energyBall.setPointSource(SOURCE_CODE_OF_MINING);
                energyBall.setPoint(BigDecimal.ZERO);
                energyBall.setPower(BigDecimal.ZERO);
                energyBall.setPowerSource(0);
                energyBall.setStatus(STATUS_OF_NEW_BORN_ENERGYBALL);
                energyBall.setTimeCreated(now);
                energyBall.setTimeUpdated(now);
                energyBallMapper.insertEnergyBall(energyBall);
            }
        }
    }

    public static void main(String[] args) {
    }
}