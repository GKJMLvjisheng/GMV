package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.*;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergySourcePoint;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;
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
     * 根据能量球uuid 更新其状态，将状态
     * @return
     */
    public int updateEnergyBallStatusByUuid(String userUuid) {
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        String uuid = checkinEnergyBall.getUuid();
        return energyBallMapper.updateStatusByUuid(uuid, STATUS_OF_DIE_ENERGYBALL, now);
    }

    /**
     * 挖矿
     * @param userUuid
     * @return
     */
    public List<EnergyBallWrapper> miningEnergyBall(String userUuid) {
        List<EnergyBall> energyBalls = energyBallMapper.selectByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING);
        EnergySourcePoint energySourcePoint = energySourcePointMapper.queryBySourceCode(SOURCE_CODE_OF_MINING);
        BigDecimal pointIncreaseSpeed = energySourcePoint.getPointIncreaseSpeed();  // 增长速度
        BigDecimal pointCapacityEachBall = energySourcePoint.getPointCapacityEachBall();      // 球最大容量
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        if (CollectionUtils.isEmpty(energyBalls)) {
            EnergyBall energyBall = setMiningEnergyBall(userUuid);
            energyBallMapper.insertEnergyBall(energyBall);
            energyBalls.add(energyBall);
            System.out.println("初始");
        }else {
            Iterator iterator = energyBalls.iterator();
            BigDecimal preTotalPoint = new BigDecimal("0");
            while (iterator.hasNext()) {
                EnergyBall energyBall = (EnergyBall) iterator.next();
                preTotalPoint = preTotalPoint.add(energyBall.getPoint());
            }
            System.out.println("preTotal:" + preTotalPoint);// 已有总量
            energyBalls.clear();
            EnergyBall latestEnergyBall = energyBallMapper.selectLatestOneByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING);
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_OF_TIME);
            long latestTime = 0;// 最近一次更新的能量球的更新时间，声明、初始化
            long currentTime = 0;   // 现在的时间，声明、初始化
            try {
                latestTime = sdf.parse(latestEnergyBall.getTimeUpdated()).getTime();
                currentTime = sdf.parse(now).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println("最新球更新时间：" + latestTime);
            long deltaTime = (currentTime - latestTime) / TRANSFER_OF_SECOND_TO_MILLISECOND; // 实际需增加的时间量
            System.out.println("时间差：" + deltaTime);
            BigDecimal pointNeeded = pointIncreaseSpeed.multiply(BigDecimal.valueOf(deltaTime));
            if (preTotalPoint.add(pointNeeded).compareTo
                    (pointCapacityEachBall.multiply(BigDecimal
                            .valueOf(MAX_COUNT_OF_MINING_ENERGYBALL))) == -1) {
                BigDecimal latestBallPoint = latestEnergyBall.getPoint();   // 最新球的积分含量
                long latestBallTime = latestBallPoint
                        .divide(pointIncreaseSpeed, 0, BigDecimal.ROUND_HALF_UP).longValue(); // 转化为最新球时间含量
                long totalTime = deltaTime + latestBallTime;// 现有的球零头加上需要增加的量
                BigDecimal totalPoint = pointIncreaseSpeed.multiply(BigDecimal.valueOf(totalTime));
                System.out.println("需要增加的总积分:" + totalPoint);
                int amount = totalPoint.divide(pointCapacityEachBall, 0, BigDecimal.ROUND_UP).intValue();
                System.out.println("total" + totalPoint);
                System.out.println("需要变更的球数：" + amount);
                if (amount > 1) {
                    energyBallMapper.updatePointByUuid(latestEnergyBall.getUuid(), pointCapacityEachBall, now);
                    energyBalls.add(energyBallMapper.selectByUuid(latestEnergyBall.getUuid()));
                    BigDecimal pointOfBalls = pointCapacityEachBall.multiply(BigDecimal.valueOf(amount));
                    BigDecimal balance = totalPoint.subtract(pointCapacityEachBall.multiply(BigDecimal.valueOf(amount-1)));
                    for (int i = 1; i < amount; i++) {
                        EnergyBall energyBall = setMiningEnergyBall(userUuid);
                        if (i != amount - 1) {
                            energyBall.setPoint(pointCapacityEachBall);
                        } else {
                            energyBall.setPoint(balance);
                        }
                        energyBallMapper.insertEnergyBall(energyBall);
                        energyBalls.add(energyBall);
                    }
                }else {
                    energyBallMapper.updatePointByUuid(latestEnergyBall.getUuid(), totalPoint, now);
                    EnergyBall energyBall = energyBallMapper.selectByUuid(latestEnergyBall.getUuid());
                    energyBalls.add(energyBall);
                }
            }else {

            }
        }
        System.out.println("需要添加的：" + energyBalls);
        List<EnergyBallWrapper> energyBallWrappers = energyBallMapper.selectPartByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING);
        System.out.println("挖矿能量球：" + energyBallWrappers);
        return energyBallWrappers;
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

    /**
     * 查询挖矿类型的所有能量球
     * @return
     */
    public List<EnergyBallWrapper> listEnergyBall(String userUuid) {
        // 如果此用户该类型能量球还没有，则生成
        this.miningEnergyBallGenerate(userUuid);
        // 查询所有挖矿能量球的部分信息，包装
        return energyBallMapper.selectPartByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING);
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

    /**
     * 产生不足数的能量球原球
     */
    public void miningEnergyBallGenerate(String userUuid) {
        // 查询该用户是否产生过挖矿能量球
        List<EnergyBall> energyBalls = energyBallMapper
                .selectByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING);
        EnergyBall energyBall = new EnergyBall();
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
                energyBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
                energyBall.setTimeCreated(now);
                energyBall.setTimeUpdated(now);
                energyBallMapper.insertEnergyBall(energyBall);
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            String prefixUUID = UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT);
            System.out.println(prefixUUID);
        }
    }
}