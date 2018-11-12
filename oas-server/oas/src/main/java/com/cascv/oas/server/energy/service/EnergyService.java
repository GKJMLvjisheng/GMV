package com.cascv.oas.server.energy.service;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.StringUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.ActivityRewardConfig;
import com.cascv.oas.server.activity.model.EnergyPointBall;
import com.cascv.oas.server.activity.model.PointTradeRecord;
import com.cascv.oas.server.activity.service.ActivityService;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.*;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.vo.*;
import com.cascv.oas.server.timezone.service.TimeZoneService;

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
    private EnergyWalletMapper energyWalletMapper;  
    @Autowired
    private UserWalletService userWalletService;
    @Autowired
    ActivityService activityService;
    @Autowired
    private ActivityMapper activityMapper;
	@Autowired 
	private TimeZoneService timeZoneService;

    //private EnergyBall checkinEnergyBall = new EnergyBall();
    //private static String checkinEnergyBallUuid;
    private static final Integer STATUS_OF_ACTIVE_ENERGYBALL = 1;       // 能量球活跃状态，可被获取
    private static final Integer STATUS_OF_DIE_ENERGYBALL = 0;          // 能量球死亡状态，不可被获取
    private static final Integer STATUS_OF_ACTIVE_ENERGYRECORD = 1;    // 能量记录活跃状态，可被获取
    private static final Integer STATUS_OF_DIE_ENERGYRECORD = 0;       // 能量记录活跃状态，不可被获取
    private static final String SOURCE_UUID_OF_CHECKIN = "CHECKIN";            // 能量球来源：签到为"CHECKIN"
    private static final String REWARD_UUID_OF_POINT = "POINT";            //签到能量球奖励来源：积分为"POINT"
    private static final String REWARD_UUID_OF_POWER = "POWER";            //签到能量球奖励来源：算力为"POWER"
    private static final String SOURCE_UUID_OF_MINING = "FREEBALL";             // 能量球来源：空投为"FREEBALL"
    private static final Integer MAX_COUNT_OF_MINING_ENERGYBALL = 8;       //空投球的最大个数
    private static final Integer TRANSFER_OF_SECOND_TO_MILLISECOND = 1000; // 秒与毫秒的转换倍率
    private static final Integer ENEGY_IN = 1;               // 能量增加为1，能量减少为0
    private static final String FORMAT_OF_TIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * Checkin the 1st function
     * 查询当日是否签过到，已签到返回true,当日尚未签到返回false
     * @param userUuid
     * @return
     * @throws ParseException 
     */
    public Boolean isCheckin(String userUuid) throws ParseException {
    	Integer timeGap = timeZoneService.getTimeGap(userUuid);
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf.parse(now);
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.HOUR_OF_DAY, timeGap);
    	String newTime = sdf.format(calendar.getTime());
    	String today = newTime.substring(0, 10);
    	log.info("today={}", today);
        List<EnergyBall> energyBalls = energyBallMapper
                .selectByTimeFuzzyQuery(userUuid, SOURCE_UUID_OF_CHECKIN, timeGap, today);
        return CollectionUtils.isEmpty(energyBalls) ? false : true;
    }

//    /**
//     * Checkin the 2nd function
//     * 插入签到EnergyBall
//     * @param userUuid
//     * @return
//     */
//    public int saveCheckinEnergyBall(String userUuid) {
//        this.setCheckinEnergyBall(userUuid);
//        checkinEnergyBallUuid = checkinEnergyBall.getUuid();
//        return energyBallMapper.insertEnergyBall(checkinEnergyBall);
//    }

    /**
     * Checkin the 3rd function
     * 插入签到记录
     * @param userUuid
     * @return
     */
//    public int saveCheckinEnergyRecord(String userUuid) {
//        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
//        EnergyTradeRecord energyTradeRecord = getEnergyRecord(userUuid, checkinEnergyBall.getUuid(), now);
//        return energyTradeRecordMapper.insertEnergyTradeRecord(energyTradeRecord);
//    }

//    /**
//     * Checkin the 4th function
//     * 在能量钱包(EnergyWallet)中添加记录
//     * @param
//     * @param userUuid
//     * @return
//     */
//    public void updateCheckinEnergyWallet(String userUuid) {
//        String uuid = energyWalletMapper.selectByUserUuid(userUuid).getUuid();
//        energyWalletMapper.increasePoint(uuid, this.getCheckinEnergy().getNewEnergyPoint());
//        energyWalletMapper.increasePower(uuid, this.getCheckinEnergy().getNewPower());
//    }

//    /**
//     * Checkin the 5th function
//     * 根据能量球uuid 更新其状态
//     * @return
//     */
//    public int updateEnergyBallStatusByUuid(String userUuid) {
//        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
//        String uuid = checkinEnergyBall.getUuid();
//        return energyBallMapper.updateStatusByUuid(uuid, STATUS_OF_DIE_ENERGYBALL, now);
//    }

    /**
     * 挖矿及查询
     * @param userUuid
     * @return
     * @throws ParseException 
     */
    public EnergyBallResult miningEnergyBall(String userUuid) throws ParseException {
        if (StringUtils.isEmpty(userUuid)) {
            return null;
        }
        //EnergySourcePoint energySourcePoint = energySourcePointMapper.queryBySourceCode(SOURCE_CODE_OF_MINING);
        ActivityRewardConfig activityRewardConfig = 
        		activityMapper.selectBaseValueBySourceCodeAndRewardCode(SOURCE_UUID_OF_MINING, REWARD_UUID_OF_POINT);
        BigDecimal pointIncreaseSpeed = activityRewardConfig.getIncreaseSpeed();            // 挖矿球增长速度
        BigDecimal pointCapacityEachBall = activityRewardConfig.getMaxValue();      // 挖矿球最大容量
        BigDecimal timeGap = pointCapacityEachBall.divide(pointIncreaseSpeed,
                0, BigDecimal.ROUND_HALF_UP);// 能量球起始时间和结束时间之差
        BigDecimal ongoingEnergySummary = this.miningGenerator(userUuid, activityRewardConfig);
        List<EnergyBallWrapper> energyBallWrappers = energyBallMapper
                .selectPartByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL,
                        timeGap.intValue());
        EnergyBallResult energyBallResult = new EnergyBallResult();
        energyBallResult.setEnergyBallList(energyBallWrappers);
        energyBallResult.setOngoingEnergySummary(ongoingEnergySummary);
        return energyBallResult;
    }

    public BigDecimal miningGenerator(String userUuid, ActivityRewardConfig activityRewardConfig) throws ParseException {
    	BigDecimal pointIncreaseSpeed = activityRewardConfig.getIncreaseSpeed();            // 挖矿球增长速度
        BigDecimal pointCapacityEachBall = activityRewardConfig.getMaxValue();      // 挖矿球最大容量
        BigDecimal timeGap = pointCapacityEachBall.divide(pointIncreaseSpeed,
                0, BigDecimal.ROUND_HALF_UP);// 能量球起始时间和结束时间之差
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);          // 当前时间
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_OF_TIME);
        BigDecimal ongoingEnergySummary = BigDecimal.ZERO;
        List<EnergyPointBall> freePointBalls = activityMapper
        		.selectByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        List<EnergyPointBall> energyPointBalls = new ArrayList<>();
        if(freePointBalls != null) {
        	for(int i=0; i<freePointBalls.size(); i++) {
        		String created = freePointBalls.get(i).getCreated();
        		Date startDate = sdf.parse(created);
        		Date endDate = this.timeCalculator(timeGap, startDate);
        		Date nowDate = sdf.parse(now);
        		BigDecimal point = freePointBalls.get(i).getPoint();
        		if (endDate.before(nowDate) && point.compareTo(BigDecimal.ZERO) == 0) {
        			String uuid = freePointBalls.get(i).getUuid();
        			activityMapper.deleteEnergyPointBall(uuid);
        			log.info("删除无效球");
        		}else {
        			energyPointBalls.add(freePointBalls.get(i));
        		}
        	}
        }
        // 最近创建的球，以下称为“最近球”
        EnergyPointBall latestEnergyPointBall = activityMapper
        		.selectLatestOneByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        if (CollectionUtils.isEmpty(energyPointBalls)) {
            // 首次登陆初始化
            EnergyPointBall energyBall = getMiningEnergyBall(userUuid, now);
            activityMapper.insertEnergyPointBall(energyBall);
            energyPointBalls.add(energyBall);
            ongoingEnergySummary = pointCapacityEachBall;
        } else {
            String latestUuid = latestEnergyPointBall.getUuid();                 // 最近球id
            BigDecimal latestPoint = latestEnergyPointBall.getPoint();           // 最近球积分
            //如果管理员在球还在生长的时候改变了球的最大值，要先判断最大值比正在生长的球的值大还是小
            if(latestPoint.compareTo(pointCapacityEachBall) == 1) {
            	//如果最近球积分比最大值大的话，先判断现在球的个数有没有到8个
            	if(energyPointBalls.size() < MAX_COUNT_OF_MINING_ENERGYBALL) {
            		//如果现在球的个数小于8个，现在的球停止生长，然后产生新的球
            		EnergyPointBall energyBall = getMiningEnergyBall(userUuid, now);
            		activityMapper.insertEnergyPointBall(energyBall);
            		ongoingEnergySummary = pointCapacityEachBall;            		
            	}else {
            		ongoingEnergySummary = BigDecimal.ZERO;
            	}
            	return ongoingEnergySummary;
            }
            BigDecimal remainPoint = pointCapacityEachBall.subtract(latestPoint); //最近球还需要这么多积分才能到最大值
            long remainTime = remainPoint.divide(pointIncreaseSpeed,0,BigDecimal.ROUND_HALF_UP).longValue();
            Date latestTimeCreated = new Date();     // 最近球创建时间初始化
            long currentTime = 0;                    // 现在的时间，声明、初始化
            try {
                latestTimeCreated = sdf.parse(latestEnergyPointBall.getCreated());// 最近球创建时间
                currentTime = sdf.parse(now).getTime();                          // 当前时间
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            // 已有的球积分总和
            int ballAmountPrevious = energyPointBalls.size();
            //BigDecimal pointPrevious = this.calculatePreviousPoints(energyPointBalls);
            energyPointBalls.clear();
            // 计算与最近球创建的时间差，即需要增加的积分+ 最近球已有积分
            long leadTime = (currentTime - latestTimeCreated.getTime()) / TRANSFER_OF_SECOND_TO_MILLISECOND; //现在到最近球的创建时间总共有多少时间
            long time = leadTime - remainTime;
            if(BigDecimal.valueOf(leadTime).compareTo(timeGap) != 1 || time < 0) {
        		BigDecimal balance = pointIncreaseSpeed.multiply(BigDecimal.valueOf(leadTime));
        		if(balance.compareTo(pointCapacityEachBall) == 1) {
        			balance = pointCapacityEachBall;
        		}        		
            	ongoingEnergySummary = pointCapacityEachBall.subtract(balance);
            	energyBallMapper.updatePointByUuid(latestUuid, balance, now);
        	}else {
//            BigDecimal leadPoint = pointIncreaseSpeed.multiply(BigDecimal.valueOf(leadTime));
//            BigDecimal realNeedPoint = leadPoint.subtract(latestPoint);
//            System.out.println("总的：" + pointPrevious.add(realNeedPoint));
//            System.out.println("满的：" + pointCapacityEachBall.multiply(BigDecimal.valueOf(MAX_COUNT_OF_MINING_ENERGYBALL)));
            // 根据该用户是否挖满矿来分情况更新
//            if (pointPrevious.add(realNeedPoint)
//                    .compareTo(this.calculateFullEnergyBallPoints()) == -1) {
            if(ballAmountPrevious < MAX_COUNT_OF_MINING_ENERGYBALL) {
                // 未挖满情况
//                long latestBallTime = latestPoint.divide(pointIncreaseSpeed,
//                        0, BigDecimal.ROUND_HALF_UP).longValue(); // 转化为最新球时间含量
//                BigDecimal pointNeededPlusLatest = pointIncreaseSpeed.multiply(BigDecimal.valueOf(leadTime));
//                int amount = pointNeededPlusLatest.divide(pointCapacityEachBall, 0, BigDecimal.ROUND_UP).intValue();
//                log.info("amount={}" + amount);
//                BigDecimal balance = pointNeededPlusLatest.subtract(pointCapacityEachBall.multiply(BigDecimal.valueOf(amount - 1)));
//                ongoingEnergySummary = pointCapacityEachBall.subtract(balance);
//                energyPointBalls.add(activityMapper.selectByUuid(latestUuid));
            	int remainBallNum = MAX_COUNT_OF_MINING_ENERGYBALL - ballAmountPrevious;
            	int amount = BigDecimal.valueOf(time).divide(timeGap, 0, BigDecimal.ROUND_UP).intValue();
            	long moreTime = time - timeGap.multiply(BigDecimal.valueOf(amount - 1)).longValue();
            	BigDecimal balance = pointIncreaseSpeed.multiply(BigDecimal.valueOf(moreTime));
            	if(balance.compareTo(pointCapacityEachBall) == 1)
        			balance = pointCapacityEachBall;
            	ongoingEnergySummary = pointCapacityEachBall.subtract(balance);
            	if(amount > remainBallNum) {
            		amount = remainBallNum + 1;
            		moreTime = time - timeGap.multiply(BigDecimal.valueOf(amount - 1)).longValue();
            		balance = pointIncreaseSpeed.multiply(BigDecimal.valueOf(moreTime));
            		if(balance.compareTo(pointCapacityEachBall) == 1)
            			balance = pointCapacityEachBall;
            		ongoingEnergySummary = pointCapacityEachBall.subtract(balance);
            	}
                if (amount > 1) {
                    energyBallMapper.updatePointByUuid(latestUuid, pointCapacityEachBall, now);
                    for (int i = 1; i < amount; i++) {
                        EnergyPointBall energyBall = getMiningEnergyBall(userUuid, now);
                        if (i != amount - 1) {
                            energyBall.setPoint(pointCapacityEachBall);
                        } else {
                            energyBall.setPoint(balance);
                        }
                        // 计算创建时间
                        BigDecimal deltaTime = timeGap.multiply(BigDecimal.valueOf(i)); // 时间差
                        Date timeCreated = timeCalculator(deltaTime, latestTimeCreated);
                        energyBall.setCreated(DateUtils.parseDateToStr(FORMAT_OF_TIME, timeCreated));
                        activityMapper.insertEnergyPointBall(energyBall);
                        //energyBallMapper.insertEnergyBall(energyBall);
                        energyPointBalls.add(energyBall);
                    }
                }else {
                    energyBallMapper.updatePointByUuid(latestUuid, balance, now);
                }
            } else {
                // 挖满情况
                int ballAmountNeeded = MAX_COUNT_OF_MINING_ENERGYBALL - ballAmountPrevious;
                energyBallMapper.updatePointByUuid(latestUuid, pointCapacityEachBall, now);
                energyPointBalls.add(activityMapper.selectByUuid(latestUuid));
                for (int i = 1; i <= ballAmountNeeded; i++) {
                    BigDecimal deltaTime = timeGap.multiply(BigDecimal.valueOf(i)); // 时间差
                    EnergyPointBall energyBall = getMiningEnergyBall(userUuid, now);
                    energyBall.setPoint(pointCapacityEachBall);
                    Date timeCreated = timeCalculator(deltaTime, latestTimeCreated);
                    energyBall.setCreated(DateUtils.parseDateToStr(FORMAT_OF_TIME, timeCreated));
                    activityMapper.insertEnergyPointBall(energyBall);
                    energyPointBalls.add(energyBall);
                    ongoingEnergySummary = BigDecimal.ZERO;
                }
            }
        	}
        }
        ongoingEnergySummary = ongoingEnergySummary.setScale(2, BigDecimal.ROUND_HALF_UP);// 保留2位小数点
        return ongoingEnergySummary;
    }

    /**
     * 计算当前所有能量球总积分
     * @param energyBalls
     * @return
     */
    public BigDecimal calculatePreviousPoints(List<EnergyPointBall> energyPointBall) {
        Iterator<EnergyPointBall> iterator = energyPointBall.iterator();
        BigDecimal pointPrevious = BigDecimal.ZERO;
        while (iterator.hasNext()) {
        	EnergyPointBall energyPointBalls = (EnergyPointBall) iterator.next();
            pointPrevious = pointPrevious.add(energyPointBalls.getPoint());
        }
        return pointPrevious;
    }
    /**
     * 计算挖矿能量球满时的能量积分
     * @return
     */
    public BigDecimal calculateFullEnergyBallPoints() {
//        BigDecimal pointCapacityEachBall = energySourcePointMapper
//                .queryBySourceCode(SOURCE_CODE_OF_MINING).getMaxValue();
        BigDecimal pointCapacityEachBall = activityMapper
        		.selectBaseValueBySourceCodeAndRewardCode(SOURCE_UUID_OF_MINING, REWARD_UUID_OF_POINT)
        		.getMaxValue();
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
     * @throws ParseException 
     */
    public EnergyBallTakenResult getEnergyBallTakenResult(String userUuid, String energyBallUuid) throws ParseException {
        if (StringUtils.isEmpty(userUuid) || StringUtils.isEmpty(energyBallUuid)) {
            log.info("userUuid or energyBallUuid is null");
            return null;
        }
        if (activityMapper.selectByUuid(energyBallUuid)
                .getStatus().equals(STATUS_OF_DIE_ENERGYBALL)) {
            log.info("该能量球已经被获取！");
            return null;
        }
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_OF_TIME);
        ActivityRewardConfig activityRewardConfig = 
        		activityMapper.selectBaseValueBySourceCodeAndRewardCode(SOURCE_UUID_OF_MINING, REWARD_UUID_OF_POINT);
        BigDecimal pointIncreaseSpeed = activityRewardConfig.getIncreaseSpeed();            // 挖矿球增长速度
        BigDecimal pointCapacityEachBall = activityRewardConfig.getMaxValue();      // 挖矿球最大容量
        BigDecimal timeGap = pointCapacityEachBall.divide(pointIncreaseSpeed,
                0, BigDecimal.ROUND_HALF_UP);// 能量球起始时间和结束时间之差
        if (activityMapper.selectByUuid(energyBallUuid).getPoint()
        		.compareTo(activityMapper.selectBaseValueBySourceCodeAndRewardCode(SOURCE_UUID_OF_MINING, REWARD_UUID_OF_POINT)
                .getMaxValue()) != 0){
        	List<EnergyPointBall> energyPointBallList = activityMapper
            		.selectByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        	String created = activityMapper.selectByUuid(energyBallUuid).getCreated();
        	Date startDate = sdf.parse(created);
    		Date endDate = this.timeCalculator(timeGap, startDate);
    		Date nowDate = sdf.parse(now);
			if(endDate.before(nowDate)) {
				// 改变被取走能量的球的状态
		        activityMapper.updatePointStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
		        // 增加记录
		        PointTradeRecord pointTradeRecord = this.getEnergyRecord(userUuid, energyBallUuid, now);
		        pointTradeRecord.setPointChange(activityMapper.selectByUuid(energyBallUuid).getPoint());
		        pointTradeRecord.setRestPoint(getPointWalletPoint(pointTradeRecord.getUserUuid(),pointTradeRecord.getInOrOut(),pointTradeRecord.getPointChange()));
		        activityMapper.insertPointTradeRecord(pointTradeRecord);
		        // 改变账户余额
		        EnergyPointBall energyPointBall = activityMapper.selectByUuid(energyBallUuid);
		        BigDecimal increasePoint;
		        if (energyPointBall == null)
		        	increasePoint = BigDecimal.ZERO;
		        else
		        	increasePoint = energyPointBall.getPoint();
		        activityMapper.increasePoint(userUuid, increasePoint, now);
		        EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
		        energyBallTakenResult.setNewEnergyPonit(increasePoint);
		        energyBallTakenResult.setNewPower(BigDecimal.ZERO);
		        // 判断采摘前球是否已经满了，如果满了，摘掉一个球马上生成一个新的
		        List<EnergyPointBall> energyBallsAfter = activityMapper
		        		.selectByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
		        EnergyPointBall latestBall = activityMapper
		        		.selectLatestOneByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
		        if (CollectionUtils.isEmpty(energyBallsAfter) ||
		        		(energyPointBallList.size() == MAX_COUNT_OF_MINING_ENERGYBALL && 
		        		latestBall.getPoint().compareTo(pointCapacityEachBall) == 0) ) {
		            activityMapper.insertEnergyPointBall(getMiningEnergyBall(userUuid, now));
		            energyBallsAfter.add(getMiningEnergyBall(userUuid, now));
		        }
		        return energyBallTakenResult;
			}else {
	            log.info("该能量球尚未满！");
	            return null;
			}
        }else {
        	List<EnergyPointBall> energyPointBallList = activityMapper
            		.selectByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
        	// 改变被取走能量的球的状态
            activityMapper.updatePointStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
            // 增加记录
            PointTradeRecord pointTradeRecord = this.getEnergyRecord(userUuid, energyBallUuid, now);
            pointTradeRecord.setPointChange(activityMapper.selectByUuid(energyBallUuid).getPoint());
            pointTradeRecord.setRestPoint(getPointWalletPoint(pointTradeRecord.getUserUuid(),pointTradeRecord.getInOrOut(),pointTradeRecord.getPointChange()));
            activityMapper.insertPointTradeRecord(pointTradeRecord);
            // 改变账户余额
            EnergyPointBall energyPointBall = activityMapper.selectByUuid(energyBallUuid);
            BigDecimal increasePoint;
            if (energyPointBall == null)
            	increasePoint = BigDecimal.ZERO;
            else
            	increasePoint = energyPointBall.getPoint();
            activityMapper.increasePoint(userUuid, increasePoint, now);
            EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
            energyBallTakenResult.setNewEnergyPonit(increasePoint);
            energyBallTakenResult.setNewPower(BigDecimal.ZERO);
            // 判断采摘前球是否已经满了，如果满了，摘掉一个球马上生成一个新的
            List<EnergyPointBall> energyBallsAfter = activityMapper
            		.selectByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
            EnergyPointBall latestBall = activityMapper
	        		.selectLatestOneByPointSourceCode(userUuid, SOURCE_UUID_OF_MINING, STATUS_OF_ACTIVE_ENERGYBALL);
            if (CollectionUtils.isEmpty(energyBallsAfter) ||
            		(energyPointBallList.size() == MAX_COUNT_OF_MINING_ENERGYBALL && 
	        		latestBall.getPoint().compareTo(pointCapacityEachBall) == 0) ) {
                activityMapper.insertEnergyPointBall(getMiningEnergyBall(userUuid, now));
                energyBallsAfter.add(getMiningEnergyBall(userUuid, now));
            }
            return energyBallTakenResult;
        }        
        
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
     * 生成进账记录
     * @param userId
     * @return
     */
    public PointTradeRecord getEnergyRecord(String userId, String energyBallUuid, String now) {
    	PointTradeRecord pointTradeRecord = new PointTradeRecord();
    	pointTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
    	pointTradeRecord.setUserUuid(userId);
    	pointTradeRecord.setEnergyBallUuid(energyBallUuid);
    	pointTradeRecord.setInOrOut(ENEGY_IN);
    	pointTradeRecord.setCreated(now);
    	pointTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYRECORD);
    	pointTradeRecord.setPointChange(activityService.getNewPoint(SOURCE_UUID_OF_MINING, REWARD_UUID_OF_POINT).getNewPoint());
    	log.info("point={}", pointTradeRecord.getPointChange());
    	pointTradeRecord.setRestPoint(getPointWalletPoint(userId,ENEGY_IN,activityService.getNewPoint(SOURCE_UUID_OF_MINING, REWARD_UUID_OF_POINT).getNewPoint()));
        return pointTradeRecord;
    }
    /**
     * 根据用户id获取用户的能量钱包point值
     * @param userId
     * @return
     */
    public BigDecimal getPointWalletPoint(String userId,Integer flag,BigDecimal changePoint) {
    	BigDecimal result = null;
    	if(userId == null) return result;
    	EnergyWallet ewallet = energyWalletMapper.selectByUserUuid(userId);
    	if(ewallet!=null) {
    		result = (flag>0?ewallet.getPoint().add(changePoint):ewallet.getPoint().subtract(changePoint));
    	}
    	return result;
    }

    /**
     * 获取签到的属性：增加积分、算力的数值
     * @return
     */
    public EnergyCheckinResult getCheckinEnergy() {
        BigDecimal point = activityMapper
        		.selectBaseValueBySourceCodeAndRewardCode(SOURCE_UUID_OF_CHECKIN, REWARD_UUID_OF_POINT)
        		.getBaseValue();
        BigDecimal power = activityMapper
        		.selectBaseValueBySourceCodeAndRewardCode(SOURCE_UUID_OF_CHECKIN, REWARD_UUID_OF_POWER)
        		.getBaseValue();
        if (point == null)
        	point = BigDecimal.ZERO;
        if (power == null)
        	power = BigDecimal.ZERO;
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
    public EnergyPointBall getMiningEnergyBall(String userUuid, String now) {
    	EnergyPointBall energyBall = new EnergyPointBall();
        energyBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
        energyBall.setUserUuid(userUuid);
        energyBall.setPoint(BigDecimal.ZERO);
        energyBall.setSourceUuid(SOURCE_UUID_OF_MINING);
        energyBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
        energyBall.setCreated(now);
        energyBall.setUpdated(now);
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

    private void saveEnergyOutRecord(String userUuid, BigDecimal total, String endTime) {
    	EnergyTradeRecord energyTradeRecord = new EnergyTradeRecord();
    	energyTradeRecord.setInOrOut(0);
    	energyTradeRecord.setPointChange(total);
    	energyTradeRecord.setEnergyBallUuid(endTime);
    	energyTradeRecord.setPowerChange(BigDecimal.ZERO);
    	energyTradeRecord.setStatus(0);
    	String now = DateUtils.getTime();
    	energyTradeRecord.setCreated(now);
    	energyTradeRecord.setUpdated(now);
    	energyTradeRecord.setUserUuid(userUuid);
    	energyTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
    	energyTradeRecord.setRestPoint(getPointWalletPoint(userUuid,0,total).add(total));
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
    
    public BigDecimal summaryInPoint(String userUuid, String yyyy_MM) {
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
    	return energyTradeRecordMapper.sumInPoint(userUuid, begin, end);
    } 
    
    public BigDecimal summaryOutPoint(String userUuid, String yyyy_MM) {
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
    	return energyTradeRecordMapper.sumOutPoint(userUuid, begin, end);
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
    	if (tradeList == null)
    		return ErrorCode.NO_AVAILABLE_POINT;
    	BigDecimal sum = BigDecimal.ZERO;
    	for (EnergyTradeRecord trade : tradeList) {
    		sum=sum.add(trade.getPointChange());
    		energyTradeRecordMapper.updateStatus(trade.getUuid(), STATUS_OF_DIE_ENERGYRECORD);
    	}
    	if (sum.compareTo(BigDecimal.ZERO) == 0)
    		return ErrorCode.NO_AVAILABLE_POINT;
    	log.info("summary {}", sum);
    	if (!decreaseBalance(userUuid, sum))
    		return ErrorCode.BALANCE_NOT_ENOUGH;
    	userWalletService.addFromEnergy(userUuid, end.substring(0, 7), sum);
    	saveEnergyOutRecord(userUuid, sum, end);
    	return ErrorCode.SUCCESS;
    } 
    
    public Integer countEnergyChange(String userUuid) {
    	return energyTradeRecordMapper.countByUserUuid(userUuid);
    }
       
    public List<EnergyChangeDetail> searchEnergyChange(String userUuid, Integer offset, Integer limit,Integer inOrOut) {
    	String srcFormater="yyyy-MM-dd HH:mm:ss";
	    String dstFormater="yyyy-MM-dd HH:mm:ss";
		String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
    if(inOrOut!=null){
    	if(inOrOut==1)
    	{ 	
    		List<EnergyChangeDetail> energyChangeDetailList = energyTradeRecordMapper.selectByPage(userUuid, offset, limit);
    		List<EnergyChangeDetail> energyList = new ArrayList<>();
    	for (EnergyChangeDetail energyChangeDetail : energyChangeDetailList){
    		   //.intValue()方法是把Integer转为Int?
    		   String created=DateUtils.string2Timezone(srcFormater, energyChangeDetail.getCreated(), dstFormater, dstTimeZoneId);
			   energyChangeDetail.setCreated(created);
			   if(energyChangeDetail.getSourceUuid() != null && energyChangeDetail.getSourceUuid().equals("WALK")) {
				   energyChangeDetail.setActivity(energyChangeDetail.getComment()+energyChangeDetail.getActivity()+energyChangeDetail.getStepNum()+"步");
			   }
			   energyChangeDetail.setValue(energyChangeDetail.getDecPoint());
    		   if(energyChangeDetail.getValue().compareTo(BigDecimal.ZERO) != 0) {
    			   energyList.add(energyChangeDetail);
       			}
    	   }
    	    return energyList;
    	}
    	else{
      		List<EnergyChangeDetail> energyChangeDetailList = energyTradeRecordMapper.selectByOutPage(userUuid, offset, limit);
      		List<EnergyChangeDetail> energyList = new ArrayList<>();
        	for (EnergyChangeDetail energyChangeDetail : energyChangeDetailList){
        		energyChangeDetail.setActivity("积分消费");
    			energyChangeDetail.setCategory("积分兑换OAS代币");                                
        		//.intValue()方法是把Integer转为Int?
    			String created=DateUtils.string2Timezone(srcFormater, energyChangeDetail.getCreated(), dstFormater, dstTimeZoneId);
 			   energyChangeDetail.setCreated(created);
 			   energyChangeDetail.setValue(energyChangeDetail.getDecPoint());
     		   if(energyChangeDetail.getValue().compareTo(BigDecimal.ZERO) != 0) {
     			   energyList.add(energyChangeDetail);
        	    }
        	}
        	return energyList;
    	}
    }
    	else {
    		List<EnergyChangeDetail> energyChangeDetailList = energyTradeRecordMapper.selectByAllPage(userUuid, offset, limit);
    		List<EnergyChangeDetail> energyList = new ArrayList<>();
    	    for (EnergyChangeDetail energyChangeDetail : energyChangeDetailList){
	    		 if(energyChangeDetail.getInOrOut()==0){
	    		 energyChangeDetail.setActivity("积分消费");
				 energyChangeDetail.setCategory("积分兑换OAS代币");
	    		 }
	    		   //.intValue()方法是把Integer转为Int?
	    		 String created=DateUtils.string2Timezone(srcFormater, energyChangeDetail.getCreated(), dstFormater, dstTimeZoneId);
				   energyChangeDetail.setCreated(created);
				   energyChangeDetail.setValue(energyChangeDetail.getDecPoint());
	    		   if(energyChangeDetail.getValue().compareTo(BigDecimal.ZERO) != 0) {
	    			   energyList.add(energyChangeDetail);
	       			}
    	   }
    	    return energyList;
    	}
    } 
    

    public static void main(String[] args) {
        BigDecimal decimal = new BigDecimal(12.12345).setScale(2, BigDecimal.ROUND_HALF_UP);
        System.out.println(decimal);
    }

}
