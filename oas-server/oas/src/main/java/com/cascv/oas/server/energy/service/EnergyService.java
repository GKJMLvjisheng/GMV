package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.StringUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.*;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergySourcePoint;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.vo.EnergyBallResult;
import com.cascv.oas.server.energy.vo.EnergyBallTakenResult;
import com.cascv.oas.server.energy.vo.EnergyBallWrapper;
import com.cascv.oas.server.energy.vo.EnergyCheckinResult;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
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
    
    @Autowired
    private UserWalletService userWalletService;
    
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
     * 插入签到记录
     * @param userUuid
     * @return
     */
    public int saveCheckinEnergyRecord(String userUuid) {
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        EnergyTradeRecord energyTradeRecord = getEnergyRecord(userUuid, checkinEnergyBall.getUuid(), now);
        return energyTradeRecordMapper.insertEnergyTradeRecord(energyTradeRecord);
    }

    /**
     * Checkin the 4th function
     * 在能量钱包(EnergyWallet)中添加记录
     * @param
     * @param userUuid
     * @return
     */
    public void updateCheckinEnergyWallet(String userUuid) {
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
     * 挖矿及查询
     * @param userUuid
     * @return
     */
    public EnergyBallResult miningEnergyBall(String userUuid) {
        if (StringUtils.isEmpty(userUuid)) {
            return null;
        }
        EnergySourcePoint energySourcePoint = energySourcePointMapper.queryBySourceCode(SOURCE_CODE_OF_MINING);
        BigDecimal pointIncreaseSpeed = energySourcePoint.getPointIncreaseSpeed();            // 挖矿球增长速度
        BigDecimal pointCapacityEachBall = energySourcePoint.getPointCapacityEachBall();      // 挖矿球最大容量
        BigDecimal timeGap = pointCapacityEachBall.divide(pointIncreaseSpeed,
                0, BigDecimal.ROUND_HALF_UP);// 能量球起始时间和结束时间之差
        BigDecimal ongoingEnergySummary = this.miningGenerator(userUuid, energySourcePoint);
        List<EnergyBallWrapper> energyBallWrappers = energyBallMapper
                .selectPartByPointSourceCode(userUuid,
                        SOURCE_CODE_OF_MINING,
                        STATUS_OF_ACTIVE_ENERGYBALL,
                        timeGap.intValue());
        EnergyBallResult energyBallResult = new EnergyBallResult();
        energyBallResult.setEnergyBallList(energyBallWrappers);
        energyBallResult.setOngoingEnergySummary(ongoingEnergySummary);
        return energyBallResult;
    }


    public BigDecimal miningGenerator(String userUuid, EnergySourcePoint energySourcePoint) {
        BigDecimal ongoingEnergySummary = new BigDecimal(0);
        List<EnergyBall> energyBalls = energyBallMapper
                .selectByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        // 最近创建的球，以下称为“最近球”
        EnergyBall latestEnergyBall = energyBallMapper
                .selectLatestOneByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);

        BigDecimal pointIncreaseSpeed = energySourcePoint.getPointIncreaseSpeed();            // 挖矿球增长速度
        BigDecimal pointCapacityEachBall = energySourcePoint.getPointCapacityEachBall();      // 挖矿球最大容量
        BigDecimal timeGap = pointCapacityEachBall.divide(pointIncreaseSpeed,
                0, BigDecimal.ROUND_HALF_UP);// 能量球起始时间和结束时间之差
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);          // 当前时间
        if (CollectionUtils.isEmpty(energyBalls)) {
            // 首次登陆初始化
            EnergyBall energyBall = getMiningEnergyBall(userUuid, now);
            energyBallMapper.insertEnergyBall(energyBall);
            energyBalls.add(energyBall);
            ongoingEnergySummary = pointCapacityEachBall;
        } else {
            String latestUuid = latestEnergyBall.getUuid();                 // 最近球id
            BigDecimal latestPoint = latestEnergyBall.getPoint();           // 最近球积分
            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_OF_TIME);
            Date latestTimeCreated = new Date();     // 最近球创建时间初始化
            long currentTime = 0;                    // 现在的时间，声明、初始化
            try {
                latestTimeCreated = sdf.parse(latestEnergyBall.getTimeCreated());// 最近球创建时间
                currentTime = sdf.parse(now).getTime();                          // 当前时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 已有的球积分总和
            int ballAmountPrevious = energyBalls.size();
            BigDecimal pointPrevious = this.calculatePreviousPoints(energyBalls);
            energyBalls.clear();
            // 计算与最近球创建的时间差，即需要增加的积分+ 最近球已有积分
            long leadTime = (currentTime - latestTimeCreated.getTime()) / TRANSFER_OF_SECOND_TO_MILLISECOND;
            BigDecimal leadPoint = pointIncreaseSpeed.multiply(BigDecimal.valueOf(leadTime));
            BigDecimal realNeedPoint = leadPoint.subtract(latestPoint);
            System.out.println("总的：" + pointPrevious.add(realNeedPoint));
            System.out.println("满的：" + pointCapacityEachBall.multiply(BigDecimal.valueOf(MAX_COUNT_OF_MINING_ENERGYBALL)));
            // 根据该用户是否挖满矿来分情况更新
            if (pointPrevious.add(realNeedPoint)
                    .compareTo(this.calculateFullEnergyBallPoints()) == -1) {
                // 未挖满情况
                long latestBallTime = latestPoint.divide(pointIncreaseSpeed,
                        0, BigDecimal.ROUND_HALF_UP).longValue(); // 转化为最新球时间含量
                BigDecimal pointNeededPlusLatest = pointIncreaseSpeed.multiply(BigDecimal.valueOf(leadTime));
                int amount = pointNeededPlusLatest.divide(pointCapacityEachBall, 0, BigDecimal.ROUND_UP).intValue();
                System.out.println("amount:" + amount);
                BigDecimal balance = pointNeededPlusLatest.subtract(pointCapacityEachBall.multiply(BigDecimal.valueOf(amount - 1)));
                ongoingEnergySummary = pointCapacityEachBall.subtract(balance);
                energyBalls.add(energyBallMapper.selectByUuid(latestUuid));
                if (amount > 1) {
                    energyBallMapper.updatePointByUuid(latestUuid, pointCapacityEachBall, now);
                    for (int i = 1; i < amount; i++) {
                        EnergyBall energyBall = getMiningEnergyBall(userUuid, now);
                        if (i != amount - 1) {
                            energyBall.setPoint(pointCapacityEachBall);
                        } else {
                            energyBall.setPoint(balance);
                        }
                        // 计算创建时间
                        BigDecimal deltaTime = timeGap.multiply(BigDecimal.valueOf(i)); // 时间差
                        Date timeCreated = timeCalculator(deltaTime, latestTimeCreated);
                        energyBall.setTimeCreated(DateUtils.parseDateToStr(FORMAT_OF_TIME, timeCreated));
                        energyBallMapper.insertEnergyBall(energyBall);
                        energyBalls.add(energyBall);
                    }
                }else {
                    energyBallMapper.updatePointByUuid(latestUuid, balance, now);
                }
            } else {
                // 挖满情况
                int ballAmountNeeded = MAX_COUNT_OF_MINING_ENERGYBALL - ballAmountPrevious;
                energyBallMapper.updatePointByUuid(latestUuid, pointCapacityEachBall, now);
                energyBalls.add(energyBallMapper.selectByUuid(latestUuid));
                for (int i = 1; i <= ballAmountNeeded; i++) {
                    BigDecimal deltaTime = timeGap.multiply(BigDecimal.valueOf(i)); // 时间差
                    EnergyBall energyBall = getMiningEnergyBall(userUuid, now);
                    energyBall.setPoint(pointCapacityEachBall);
                    Date timeCreated = timeCalculator(deltaTime, latestTimeCreated);
                    energyBall.setTimeCreated(DateUtils.parseDateToStr(FORMAT_OF_TIME, timeCreated));
                    energyBallMapper.insertEnergyBall(energyBall);
                    energyBalls.add(energyBall);
                    ongoingEnergySummary = BigDecimal.ZERO;
                }
            }
        }
        return ongoingEnergySummary;
    }

    /**
     * 计算当前所有能量球总积分
     * @param energyBalls
     * @return
     */
    public BigDecimal calculatePreviousPoints(List<EnergyBall> energyBalls) {
        Iterator iterator = energyBalls.iterator();
        BigDecimal pointPrevious = new BigDecimal("0");
        while (iterator.hasNext()) {
            EnergyBall energyBall = (EnergyBall) iterator.next();
            pointPrevious = pointPrevious.add(energyBall.getPoint());
        }
        return pointPrevious;
    }
    /**
     * 计算挖矿能量球满时的能量积分
     * @return
     */
    public BigDecimal calculateFullEnergyBallPoints() {
        BigDecimal pointCapacityEachBall = energySourcePointMapper
                .queryBySourceCode(SOURCE_CODE_OF_MINING).getPointCapacityEachBall();
        BigDecimal fullPoints = pointCapacityEachBall
                .multiply(BigDecimal.valueOf(MAX_COUNT_OF_MINING_ENERGYBALL));
        return fullPoints;
    }

    /**
     * 时间计算
     * @param timeGap
     * @param time
     * @return
     */
    public Date timeCalculator(BigDecimal timeGap, Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.SECOND, timeGap.intValue());
        return calendar.getTime();
    }

    /**
     * 提取挖矿所得
     * @param userUuid
     * @param energyBallUuid
     * @return
     */
    public EnergyBallTakenResult getEnergyBallTakenResult(String userUuid, String energyBallUuid) {
        if (StringUtils.isEmpty(userUuid) || StringUtils.isEmpty(energyBallUuid)) {
            System.out.println("userUuid or energyBallUuid is null");
            return null;
        }
        List<EnergyBall> energyBallsBefore = energyBallMapper
                .selectByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        // 改变被取走能量的球的状态
        energyBallMapper.updateStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
        // 增加记录
        EnergyTradeRecord energyTradeRecord = getEnergyRecord(userUuid, energyBallUuid, now);
        energyTradeRecord.setPointChange(energyBallMapper.selectByUuid(energyBallUuid).getPoint());
        energyTradeRecord.setPowerChange(energyBallMapper.selectByUuid(energyBallUuid).getPower());
        energyTradeRecordMapper.insertEnergyTradeRecord(energyTradeRecord);
        // 改变账户余额
        EnergyBall energyBall = energyBallMapper.selectByUuid(energyBallUuid);
        BigDecimal increasePoint = energyBall.getPoint();
        BigDecimal increasePower = energyBall.getPower();
        String uuid = new String();
        if (energyWalletMapper.selectByUserUuid(userUuid) == null) {
            EnergyWallet energyWallet = this.getEnergyWallet(userUuid, now);
            uuid = energyWallet.getUuid();
            energyWalletMapper.insertSelective(energyWallet);
        }else {
            uuid = energyWalletMapper.selectByUserUuid(userUuid).getUuid();
        }
        energyWalletMapper.increasePoint(uuid, increasePoint);
        energyWalletMapper.increasePower(uuid, increasePower);
        energyBallTakenResult.setNewEnergyPonit(increasePoint);
        energyBallTakenResult.setNewPower(increasePower);
        // 判断能量球有没有被拿光，如果是，要产生一个新的
        // 判断采摘前球是否已经满了，如果满了，摘掉一个球马上生成一个新的
        List<EnergyBall> energyBallsAfter = energyBallMapper
                .selectByPointSourceCode(userUuid, SOURCE_CODE_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        if (CollectionUtils.isEmpty(energyBallsAfter) ||
                this.calculatePreviousPoints(energyBallsBefore).compareTo(this.calculateFullEnergyBallPoints()) ==0 ) {
            energyBallMapper.insertEnergyBall(getMiningEnergyBall(userUuid, now));
            energyBallsAfter.add(getMiningEnergyBall(userUuid, now));
        }
        return energyBallTakenResult;
    }


    // 以下为非业务方法
    /**
     * 根据用户uuid查询用户当前积分和算力
     * @return
     */
    public EnergyWallet findByUserUuid(String userUuid) {
        return energyWalletMapper.selectByUserUuid(userUuid);
    }

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
     * 生成进账记录
     * @param userId
     * @return
     */
    public EnergyTradeRecord getEnergyRecord(String userId, String energyBallUuid, String now) {
        EnergyTradeRecord energyTradeRecord = new EnergyTradeRecord();
        energyTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
        energyTradeRecord.setUserUuid(userId);
        energyTradeRecord.setEnergyBallUuid(energyBallUuid);
        energyTradeRecord.setInOrOut(ENEGY_IN);
        energyTradeRecord.setTimeCreated(now);
        energyTradeRecord.setTimeUpdated(now);
        energyTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYRECORD);
        energyTradeRecord.setPointChange(this.getCheckinEnergy().getNewEnergyPoint());
        energyTradeRecord.setPowerChange(this.getCheckinEnergy().getNewPower());
        return energyTradeRecord;
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
     * 设置新增挖矿能量球
     * @param userUuid
     * @return
     */
    public EnergyBall getMiningEnergyBall(String userUuid, String now) {
        EnergyBall energyBall = new EnergyBall();
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
     * 设置新增钱包
     * @param userUuid
     * @param now
     * @return
     */
    public EnergyWallet getEnergyWallet(String userUuid, String now) {
        EnergyWallet energyWallet = new EnergyWallet();
        energyWallet.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        energyWallet.setUserUuid(userUuid);
        energyWallet.setPoint(BigDecimal.ZERO);
        energyWallet.setPower(BigDecimal.ZERO);
        energyWallet.setCreated(now);
        energyWallet.setUpdated(now);
        return energyWallet;
    }

    private void saveEnergyOutRecord(String userUuid, BigDecimal total) {
    	EnergyTradeRecord energyTradeRecord = new EnergyTradeRecord();
    	energyTradeRecord.setInOrOut(0);
    	energyTradeRecord.setPointChange(total);
    	energyTradeRecord.setEnergyBallUuid("0");
    	energyTradeRecord.setPowerChange(BigDecimal.ZERO);
    	energyTradeRecord.setStatus(0);
    	String now = DateUtils.getTime();
    	energyTradeRecord.setTimeCreated(now);
    	energyTradeRecord.setTimeUpdated(now);
    	energyTradeRecord.setUserUuid(userUuid);
    	energyTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
    	energyTradeRecordMapper.insertEnergyTradeRecord(energyTradeRecord);
    }
    /*
     * summary period point
     * */
    public BigDecimal summaryPoint(String userUuid, String yyyy_MM) {
        String begin = yyyy_MM + "-01 00:00:00";
        String end = null;
    	String today = DateUtils.dateTimeNow(DateUtils.YYYY_MM);
    	if (yyyy_MM.compareToIgnoreCase(today) == 0) {
    		end=DateUtils.getTime();
    	} else {
    		Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.dateTime(DateUtils.YYYY_MM, yyyy_MM));
    		int day = calendar.getActualMaximum(Calendar.DATE);
        	calendar.set(Calendar.DAY_OF_MONTH, day);
        	end = yyyy_MM + String.format("-%02d 23:59:59", day);
    	}
    	log.info("begin {} end {}", begin, end);
    	return energyTradeRecordMapper.sumPoint(userUuid, begin, end);
    } 
    
    //redeem
    public Boolean decreaseBalance(String userUuid, BigDecimal value) {
      EnergyWallet energyWallet = energyWalletMapper.selectByUserUuid(userUuid);
      if (energyWallet == null|| energyWallet.getPoint().compareTo(value) < 0) {
        return false;
      }
      energyWalletMapper.decreasePoint(energyWallet.getUuid(), value);
      return true;
    }
    
    public ErrorCode redeem(String userUuid, String yyyy_MM) {
        String begin = yyyy_MM + "-01 00:00:00";
        String end = null;
    	String today = DateUtils.dateTimeNow(DateUtils.YYYY_MM);
    	if (yyyy_MM.compareToIgnoreCase(today) == 0) {
    		end=DateUtils.getTime();
    	} else {
    		Calendar calendar = Calendar.getInstance();
            calendar.setTime(DateUtils.dateTime(DateUtils.YYYY_MM, yyyy_MM));
    		int day = calendar.getActualMaximum(Calendar.DATE);
        	calendar.set(Calendar.DAY_OF_MONTH, day);
        	end = yyyy_MM + String.format("-%02d 23:59:59", day);
    	}
    	
    	List<EnergyTradeRecord> tradeList = energyTradeRecordMapper.selectTrade(userUuid, begin, end);
    	BigDecimal sum = BigDecimal.ZERO;
    	for (EnergyTradeRecord trade : tradeList) {
    		sum=sum.add(trade.getPointChange());
    		energyTradeRecordMapper.updateStatus(trade.getUuid(), STATUS_OF_DIE_ENERGYRECORD);
    	}
    	log.info("summary {}", sum);
    	if (!decreaseBalance(userUuid, sum))
    		return ErrorCode.BALANCE_NOT_ENOUGH;
    	userWalletService.addFromEnergy(userUuid, sum);
    	saveEnergyOutRecord(userUuid, sum);
    	return ErrorCode.SUCCESS;
    } 
    
    public static void main(String[] args) {
    }
}