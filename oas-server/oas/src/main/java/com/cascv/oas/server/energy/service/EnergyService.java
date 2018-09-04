package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.*;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergySourcePoint;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.vo.EnergyBallResult;
import com.cascv.oas.server.energy.vo.EnergyBallWrapper;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;
import org.apache.commons.collections.CollectionUtils;
import org.bouncycastle.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private EnergyWalletMapper energyWalletMapper;
    private static String checkinEnergyBallUuid;
    private EnergyBall checkinEnergyBall = new EnergyBall();

    private static final Integer STATUS_OF_ACTIVE_ENERGYBALL = 1;       // 能量球活跃状态，可被获取
    private static final Integer STATUS_OF_DIE_ENERGYBALL = 0;          // 能量球死亡状态，不可被获取
    private static final Integer STATUS_OF_ACTIVE_ENERGYRECORD = 1;    // 能量记录活跃状态，可被获取
    private static final Integer STATUS_OF_DIE_ENERGYRECORD = 0;       // 能量记录活跃状态，不可被获取
    private static final Integer SOURCE_CODE_OF_NONE = 0;               // 能量球来源，无
    private static final Integer SOURCE_CODE_OF_CHECKIN = 1;            // 能量球来源：签到为1
    private static final Integer SOURCE_CODE_OF_MINING = 2;             // 能量球来源：挖矿为2
    private static final Integer MAX_COUNT_OF_MINING_ENERGYBALL = 16;
    private static final Integer TRANSFER_OF_SECOND_TO_MILLISECOND = 1000; // 秒与毫秒的转换倍率
    private static final Integer ENEGY_IN = 1;               // 能量增加为1，能量减少为0
    private static final String FORMAT_OF_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * Checkin the 1st function
     * 查询当日是否签过到，已签到返回true,当日尚未签到返回false
     * @param userUuid
     * @return
     */
    public Boolean isCheckin(String userUuid) {
        String today = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
        List<EnergyBall> energyBalls = energyBallMapper
                .selectByTimeFuzzyQuery(userUuid, SOURCE_CODE_OF_CHECKIN, today);
        return CollectionUtils.isEmpty(energyBalls) ? false : true;
    }

    /**
     * Checkin the 2nd function
     * 插入签到EnergyBall
     * @param userUuid
     * @return
     */
    public int saveCheckinEnergyBall(String userUuid) {
        this.setCheckinEnergyBall(userUuid);
        checkinEnergyBallUuid = checkinEnergyBall.getUuid();
        return energyBallMapper.insertEnergyBall(checkinEnergyBall);
    }

    /**
     * Checkin the 3rd function
     * 能量记录表中插入签到记录
     * @param userId
     * @return
     */
    public int saveEnergyRecord(String userId) {
        EnergyTradeRecord energyTradeRecord = new EnergyTradeRecord();
        energyTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
        energyTradeRecord.setUserUuid(userId);
        energyTradeRecord.setEnergyBallUuid(checkinEnergyBallUuid);
        energyTradeRecord.setInOrOut(ENEGY_IN);
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        energyTradeRecord.setTimeCreated(now);
        energyTradeRecord.setTimeUpdated(now);
        energyTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYRECORD);
        energyTradeRecord.setPointChange(this.getCheckinEnergy().getNewEnergyPoint());
        energyTradeRecord.setPowerChange(this.getCheckinEnergy().getNewPower());
        return energyTradeRecordMapper.insertEnergyTradeRecord(energyTradeRecord);
    }

    /**
     * Checkin the 4th function
     * 在能量钱包(EnergyWallet)中添加记录
     * @param
     * @param userUuid
     * @return
     */
    public void updateEnergyWallet(String userUuid) {
        String uuid = energyWalletMapper.selectByUserUuid(userUuid).getUuid();
        energyWalletMapper.increasePoint(uuid, this.getCheckinEnergy().getNewEnergyPoint());
        energyWalletMapper.increasePower(uuid, this.getCheckinEnergy().getNewPower());
    }

    /**
     * Checkin the 5th function
     * 根据能量球uuid 更新其状态
     * @return
     */
    public int updateEnergyBallStatusByUuid(String userUuid) {
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        String uuid = checkinEnergyBall.getUuid();
        return energyBallMapper.updateStatusByUuid(uuid, STATUS_OF_DIE_ENERGYBALL, now);
    }

    /**
     * 根据用户uuid查询用户当前积分和算力
     * @return
     */
    public EnergyWallet findByUserUuid(String userUuid) {
  	  return energyWalletMapper.selectByUserUuid(userUuid);
    }

    /**
     * for inquireEnergyBall
     * @param userUuid
     * @return
     */
    public EnergyBallResult miningEnergyBall(String userUuid) {
        BigDecimal ongoingEnergySummary = new BigDecimal("0");

        List<EnergyBall> energyBalls = energyBallMapper
                .selectByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        EnergyBall latestEnergyBall = energyBallMapper
                .selectLatestOneByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        EnergySourcePoint energySourcePoint = energySourcePointMapper.queryBySourceCode(SOURCE_CODE_OF_MINING);
        String latestUuid = latestEnergyBall.getUuid();                 // 最近球id
        BigDecimal latestPoint = latestEnergyBall.getPoint();           // 最近球积分
        String latestTimeUpdated = latestEnergyBall.getTimeUpdated();   // 最近球更新时间
        BigDecimal pointIncreaseSpeed = energySourcePoint.getPointIncreaseSpeed();            // 挖矿球增长速度
        BigDecimal pointCapacityEachBall = energySourcePoint.getPointCapacityEachBall();      // 挖矿球最大容量
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);          // 当前时间
        if (CollectionUtils.isEmpty(energyBalls)) {
            // 首次登陆初始化
            EnergyBall energyBall = setMiningEnergyBall(userUuid);
            energyBallMapper.insertEnergyBall(energyBall);
            energyBalls.add(energyBall);
        }else {
            // 已有的球积分总和
            Iterator iterator = energyBalls.iterator();
            BigDecimal pointPrevious = new BigDecimal("0");
            while (iterator.hasNext()) {
                EnergyBall energyBall = (EnergyBall) iterator.next();
                pointPrevious = pointPrevious.add(energyBall.getPoint());
            }
            int ballAmountPrevious = energyBalls.size();
            energyBalls.clear();
            // 计算时间差，即需要增加的积分
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_OF_TIME);
            long latestTime = 0;    // 最近一次更新的能量球的更新时间，声明、初始化
            long currentTime = 0;   // 现在的时间，声明、初始化
            try {
                latestTime = sdf.parse(latestTimeUpdated).getTime();
                currentTime = sdf.parse(now).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long deltaTime = (currentTime - latestTime) / TRANSFER_OF_SECOND_TO_MILLISECOND; // 实际需增加的时间量
            BigDecimal pointNeeded = pointIncreaseSpeed.multiply(BigDecimal.valueOf(deltaTime));
            // 根据该用户是否挖满矿来分情况更新
            if (pointPrevious.add(pointNeeded).compareTo
                    (pointCapacityEachBall.multiply(BigDecimal
                            .valueOf(MAX_COUNT_OF_MINING_ENERGYBALL))) == -1) {
                // 未挖满情况
                long latestBallTime = latestPoint
                        .divide(pointIncreaseSpeed, 0, BigDecimal.ROUND_HALF_UP).longValue(); // 转化为最新球时间含量
                long totalTime = deltaTime + latestBallTime;// 现有的球零头加上需要增加的量
                BigDecimal pointNeededPlusLatest = pointIncreaseSpeed.multiply(BigDecimal.valueOf(totalTime));
                int amount = pointNeededPlusLatest.divide(pointCapacityEachBall, 0, BigDecimal.ROUND_UP).intValue();
                System.out.println("需要变更的球数：" + amount);
                if (amount > 1) {
                    energyBallMapper.updatePointByUuid(latestUuid, pointCapacityEachBall, now);
                    energyBalls.add(energyBallMapper.selectByUuid(latestUuid));
                    BigDecimal balance = pointNeededPlusLatest.subtract(pointCapacityEachBall.multiply(BigDecimal.valueOf(amount-1)));
                    for (int i = 1; i < amount; i++) {
                        EnergyBall energyBall = setMiningEnergyBall(userUuid);
                        if (i != amount - 1) {
                            energyBall.setPoint(pointCapacityEachBall);
                        } else {
                            energyBall.setPoint(balance);
                            ongoingEnergySummary = pointCapacityEachBall.subtract(balance);
                        }
                        energyBallMapper.insertEnergyBall(energyBall);
                        energyBalls.add(energyBall);
                    }
                }else {
                    energyBallMapper.updatePointByUuid(latestUuid, pointNeededPlusLatest, now);
                    EnergyBall energyBall = energyBallMapper.selectByUuid(latestUuid);
                    energyBalls.add(energyBall);
                }
            }else {
                // 挖满情况
                int ballAmountNeeded = MAX_COUNT_OF_MINING_ENERGYBALL - ballAmountPrevious;
                int i1 = energyBallMapper.updatePointByUuid(latestUuid, pointCapacityEachBall, now);
                energyBalls.add(energyBallMapper.selectByUuid(latestUuid));
                for (int i = 0; i < ballAmountNeeded; i++) {
                    EnergyBall energyBall = setMiningEnergyBall(userUuid);
                    energyBall.setPoint(pointCapacityEachBall);
                    energyBallMapper.insertEnergyBall(energyBall);
                    energyBalls.add(energyBall);
                    ongoingEnergySummary = BigDecimal.ZERO;
                }
            }
        }
        List<EnergyBallWrapper> energyBallWrappers = energyBallMapper.selectPartByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING);
        EnergyBallResult energyBallResult = new EnergyBallResult();
        energyBallResult.setEnergyBallList(energyBallWrappers);
        energyBallResult.setOngoingEnergySummary(ongoingEnergySummary);
        return energyBallResult;
    }
    public EnergyBall setMiningEnergyBall(String userUuid) {
        EnergyBall energyBall = new EnergyBall();
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        energyBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        energyBall.setUserUuid(userUuid);
        energyBall.setPoint(BigDecimal.ZERO);
        energyBall.setPointSource(SOURCE_CODE_OF_MINING);
        energyBall.setPower(BigDecimal.ZERO);
        energyBall.setPowerSource(SOURCE_CODE_OF_NONE);
        energyBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
        energyBall.setTimeCreated(now);
        energyBall.setTimeUpdated(now);
        return energyBall;
    }

    // 以下为非业务方法
    /**
     * 产生签到EnergyBall
     * @param userUuid
     * @return
     */
    public void setCheckinEnergyBall(String userUuid) {
        checkinEnergyBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        checkinEnergyBall.setUserUuid(userUuid);
        checkinEnergyBall.setPointSource(SOURCE_CODE_OF_CHECKIN);
        checkinEnergyBall.setPowerSource(SOURCE_CODE_OF_CHECKIN);
        checkinEnergyBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);

        EnergyCheckinResult checkinEnergy = this.getCheckinEnergy();
        checkinEnergyBall.setPoint(checkinEnergy.getNewEnergyPoint());
        checkinEnergyBall.setPower(checkinEnergy.getNewPower());

        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        checkinEnergyBall.setTimeCreated(now);
        checkinEnergyBall.setTimeUpdated(now);
    }

    /**
     * 获取签到的属性：增加积分、算力的数值
     * @return
     */
    public EnergyCheckinResult getCheckinEnergy() {
        BigDecimal point = energySourcePointMapper.queryPointSingle(SOURCE_CODE_OF_CHECKIN);
        BigDecimal power = energySourcePowerMapper.queryPowerSingle(SOURCE_CODE_OF_CHECKIN);
        EnergyCheckinResult energyCheckinResult = new EnergyCheckinResult();
        energyCheckinResult.setNewEnergyPoint(point);
        energyCheckinResult.setNewPower(power);
        return energyCheckinResult;
    }


    public static void main(String[] args) {
    }
}