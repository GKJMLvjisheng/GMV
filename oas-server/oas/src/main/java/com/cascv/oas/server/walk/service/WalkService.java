package com.cascv.oas.server.walk.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.StringUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.ActivityRewardConfig;
import com.cascv.oas.server.activity.model.EnergyPointBall;
import com.cascv.oas.server.activity.model.PointTradeRecord;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergyWalletMapper;
import com.cascv.oas.server.energy.model.EnergyWallet;
import com.cascv.oas.server.energy.service.EnergyService;
import com.cascv.oas.server.energy.vo.EnergyBallTakenResult;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.walk.mapper.WalkMapper;
import com.cascv.oas.server.walk.model.WalkBall;
import com.cascv.oas.server.walk.wrapper.StepNumQuota;
import com.cascv.oas.server.walk.wrapper.StepPointQuota;
import com.cascv.oas.server.walk.wrapper.WalkBallReturn;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WalkService {
	
	@Autowired
	private WalkMapper walkMapper;
	
	@Autowired
	private ActivityMapper activityMapper;
	
	@Autowired
	private MinerMapper minerMapper;
	
	@Autowired
	private EnergyWalletMapper energyWalletMapper;
	
	@Autowired
	private EnergyService energyService;
	
	private static final String SOURCE_UUID_OF_WALKING = "WALK";             // 能量球来源：计步为3
	private static final String REWARD_UUID_OF_WALKING_POINT = "POINT";             //能量球奖励：计步奖励积分，为1
	private static final Integer STATUS_OF_ACTIVE_ENERGYBALL = 1;         //能量球活跃状态，可被获取
	private static final Integer STATUS_OF_DIE_ENERGYBALL = 0;            //能量球死亡状态，不可被获取
	private static final Integer ENERGY_IN = 1;                            //表示能量球的进账
	private static final Integer SYSTEM_PARAMETER_CURRENCY = 11;                //表示系统变量β的编号
	
	private EnergyPointBall energyPointBall = new EnergyPointBall();
	private PointTradeRecord pointTradeRecord = new PointTradeRecord();
	private WalkBall walkBall = new WalkBall();
	
	/**
     * 得到日期对应的增加的point
	 * @param userUuid 
	 * @param stepNum 
     * @return
     */
	public List<StepPointQuota> getPoint(List<StepNumQuota> quota, String userUuid) {
		ActivityRewardConfig activityRewardConfig = activityMapper.selectBaseValueBySourceCodeAndRewardCode(SOURCE_UUID_OF_WALKING, REWARD_UUID_OF_WALKING_POINT);
		List<StepPointQuota> stepPointQuotaList = new ArrayList<>();
		for(int i=0; i<quota.size(); i++){
			StepPointQuota stepPointQuota = new StepPointQuota();
			BigDecimal stepNum = quota.get(i).getStepNum();			
			BigDecimal pointBefore = activityRewardConfig.getIncreaseSpeed().multiply(stepNum);
			EnergyWallet energyWallet = energyService.findByUserUuid(userUuid);
			BigDecimal powerSum = energyWallet.getPower();
			if(powerSum.compareTo(BigDecimal.ZERO) == 0) {
				powerSum = BigDecimal.ONE;
			}
			log.info("powerSum={}", powerSum);
			BigDecimal β = minerMapper.selectSystemParameterByCurrency(SYSTEM_PARAMETER_CURRENCY).getParameterValue();
			BigDecimal point = (pointBefore.multiply(powerSum)).divide(β, 2, BigDecimal.ROUND_HALF_UP);	
			log.info("point={}", point);
			BigDecimal maxValue = activityRewardConfig.getMaxValue().multiply(powerSum).divide(β, 2, BigDecimal.ROUND_HALF_UP);
			BigDecimal newPoint;
			if(point.compareTo(maxValue) == -1)
				newPoint = point;
			else
				newPoint = maxValue;
			stepPointQuota.setPoint(newPoint);
			stepPointQuota.setDate(quota.get(i).getDate());
			stepPointQuotaList.add(stepPointQuota);
		}
		return stepPointQuotaList;	
	}
	
	/**
     * 记录一个行走步数
     * @param stepNum
     * @return
     */
	public String returnWalkBallUuid(String userUuid, StepNumQuota stepNumQuota) {
		WalkBall oneWalkBall = new WalkBall();
		 oneWalkBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
		 log.info("uuid={}", oneWalkBall.getUuid());
		 oneWalkBall.setUserUuid(userUuid);
		 oneWalkBall.setStepNum(stepNumQuota.getStepNum());
		 oneWalkBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		 oneWalkBall.setCreated(stepNumQuota.getDate());
		 oneWalkBall.setUpdated(stepNumQuota.getDate());
		 walkMapper.insertWalkBall(oneWalkBall);
		 return oneWalkBall.getUuid();
	}
	
	
	/**
     * 记录列表行走步数
     * @param stepNum
     * @return
     */
	public void addWalkPointBall(String userUuid, List<StepNumQuota> quota) {
		for(int i=0; i<quota.size(); i++){
			 walkBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
			 log.info("uuid={}", walkBall.getUuid());
			 walkBall.setUserUuid(userUuid);
			 walkBall.setStepNum(quota.get(i).getStepNum());
			 
//			 BigDecimal stepNum = quota.get(i).getStepNum();
//			 if(!(stepNum.compareTo(new BigDecimal(10000)) == 1)) {
//				 walkBall.setStepNum(stepNum);
//			 }else {
//				 walkBall.setStepNum(new BigDecimal(10000));
//			 }
			 log.info("stepNum={}", walkBall.getStepNum());
			 
			 walkBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
			 walkBall.setCreated(quota.get(i).getDate());
			 walkBall.setUpdated(quota.get(i).getDate());
			 walkMapper.insertWalkBall(walkBall);
		}		
	}
	
	/**
     * 记录一个行走能量球
     * @param stepNum userUuid
     * @return
     */
	
	public void addOneEnergyPointBall(String uuid, String userUuid, StepNumQuota stepNumQuota) {
		EnergyPointBall newEnergyPointBall = new EnergyPointBall();
		newEnergyPointBall.setUuid(uuid);
		log.info("uuid={}", newEnergyPointBall.getUuid());
		newEnergyPointBall.setUserUuid(userUuid);
		newEnergyPointBall.setSourceUuid(SOURCE_UUID_OF_WALKING);
		newEnergyPointBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		
		List<StepNumQuota> stepNumQuotaList = new ArrayList<>();
		stepNumQuotaList.add(stepNumQuota);
		newEnergyPointBall.setPoint(this.getPoint(stepNumQuotaList, userUuid).get(0).getPoint());
		
		newEnergyPointBall.setCreated(this.getPoint(stepNumQuotaList, userUuid).get(0).getDate());
		newEnergyPointBall.setUpdated(this.getPoint(stepNumQuotaList, userUuid).get(0).getDate());
		
		activityMapper.insertEnergyPointBall(newEnergyPointBall);
	}
	
	/**
     * 记录多个行走能量球
     * @param stepNum userUuid
     * @return
     */
	public void addEnergyPointBall(String userUuid, List<StepNumQuota> quota) {	
		for(int i=0; i<quota.size(); i++) {
			energyPointBall.setUuid(walkBall.getUuid());
			log.info("uuid={}", energyPointBall.getUuid());
			energyPointBall.setUserUuid(userUuid);
			energyPointBall.setSourceUuid(SOURCE_UUID_OF_WALKING);
			energyPointBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
			
			energyPointBall.setPoint(this.getPoint(quota, userUuid).get(i).getPoint());
			
			energyPointBall.setCreated(walkBall.getCreated());
			energyPointBall.setUpdated(walkBall.getUpdated());
			
			activityMapper.insertEnergyPointBall(energyPointBall);
		}
		
		
	}
	
	/**
     * 插入行走能量球记录
	 * @param userUuid 
	 * @param point 
	 * @param energyBallUuid 
     * @return
     */
	public void addPointTradeRecord(String userUuid, String energyBallUuid, BigDecimal point) {
			pointTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
			pointTradeRecord.setUserUuid(userUuid);
			pointTradeRecord.setInOrOut(ENERGY_IN);
			pointTradeRecord.setEnergyBallUuid(energyBallUuid);
			pointTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
			pointTradeRecord.setPointChange(point);
			
			String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
			pointTradeRecord.setCreated(now);
			pointTradeRecord.setRestPoint(energyService.getPointWalletPoint(userUuid,ENERGY_IN,point));
			activityMapper.insertPointTradeRecord(pointTradeRecord);
	}
	
	/**
     * 查询行走能量球
     * @param userUuid
     * @return
     */
	public List<WalkBallReturn> inquireWalkPointBall(String userUuid, List<StepNumQuota> quota) {
		if (StringUtils.isEmpty(userUuid)) {
            return null;
        }
		List<WalkBall> walkBallList = walkMapper.selectWalkBall(userUuid);
		//如果没有球，则产生球
		if(walkBallList.size() == 0) {
			this.addWalkPointBall(userUuid, quota);
			this.addEnergyPointBall(userUuid, quota);
		}else{
			HashMap<String, WalkBall> walkBallMap= new HashMap<>(); 
			for(int i=0; i<walkBallList.size(); i++) {
				walkBallMap.put(walkBallList.get(i).getCreated(), walkBallList.get(i));
			}
			for(int i=0; i<quota.size(); i++) {
				if(!walkBallMap.containsKey(quota.get(i).getDate())) {
					String uuid = this.returnWalkBallUuid(userUuid, quota.get(i));
					this.addOneEnergyPointBall(uuid, userUuid, quota.get(i));
					WalkBall newWalkBall = walkMapper.selectWalkBallbyUuid(uuid);
					walkBallMap.put(quota.get(i).getDate(), newWalkBall);
				}else{
					String date = quota.get(i).getDate();
					if(walkBallMap.get(date).getStatus() != 0) {
						walkMapper.updateStepNumByCreated(userUuid, quota.get(i).getStepNum(), date);
						List<StepNumQuota> stepNumQuotaList = new ArrayList<>();
						stepNumQuotaList.add(quota.get(i));
						walkMapper.updatePointByuuid(walkBallMap.get(date).getUuid(), this.getPoint(stepNumQuotaList, userUuid).get(0).getPoint());
						walkBallMap.get(date).setStepNum(quota.get(i).getStepNum());
						walkBallMap.put(date, walkBallMap.get(date));
					}	
				}
			}
		}
		List<WalkBallReturn> walkBallReturnList = walkMapper.selectEnergyBallList(userUuid, SOURCE_UUID_OF_WALKING, STATUS_OF_ACTIVE_ENERGYBALL);
		return walkBallReturnList;
		
	}
	
	/**
     * 采集行走能量球
	 * @param userUuid 
	 * @param energyBallUuid 
     * @return
	 * @throws ParseException 
     */
	public EnergyBallTakenResult takeWalkPointBall(String userUuid, String energyBallUuid) throws ParseException {
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		if (StringUtils.isEmpty(userUuid) || StringUtils.isEmpty(energyBallUuid)) {
            log.info("userUuid or energyBallUuid is null");
            return null;
        }
		Integer status = activityMapper.selectByUuid(energyBallUuid).getStatus();
		log.info("status={}", status);
        if (activityMapper.selectByUuid(energyBallUuid).getStatus() == STATUS_OF_DIE_ENERGYBALL) {
            log.info("该能量球已经被获取！");
            return null;
        }
        BigDecimal value = activityMapper
        		.selectBaseValueBySourceCodeAndRewardCode(SOURCE_UUID_OF_WALKING, REWARD_UUID_OF_WALKING_POINT).getMaxValue();
        BigDecimal power = BigDecimal.ONE;
        if(energyWalletMapper.selectByUserUuid(userUuid).getPower().compareTo(BigDecimal.ZERO) != 0) {
        	power = energyWalletMapper.selectByUserUuid(userUuid).getPower();
        }
        BigDecimal β = minerMapper.selectSystemParameterByCurrency(SYSTEM_PARAMETER_CURRENCY).getParameterValue();
        BigDecimal maxValue = value.multiply(power).divide(β, 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal point = activityMapper.selectByUuid(energyBallUuid).getPoint();
        //如果计步球的积分满值，则可以采集计步球，但不生成新的计步球
        if(point.compareTo(maxValue) == 0) {
        	//改变被取走的能量球的状态
            activityMapper.updatePointStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
            //改变被取走的计步球的状态
            walkMapper.updateStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
            //在交易记录表中增加取走的球
            this.addPointTradeRecord(userUuid, energyBallUuid, activityMapper.selectByUuid(energyBallUuid).getPoint());
            //更新用户能量钱包
            activityMapper.increasePoint(userUuid, activityMapper.selectByUuid(energyBallUuid).getPoint(), now);
            
            EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
            energyBallTakenResult.setNewEnergyPonit(activityMapper.selectByUuid(energyBallUuid).getPoint());
            energyBallTakenResult.setNewPower(BigDecimal.ZERO);
    		return energyBallTakenResult;
        }else {
        	SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date bt = sdf.parse(activityMapper.selectByUuid(energyBallUuid).getCreated());
            Date oldNow = format.parse(now);
            Calendar cal = Calendar.getInstance();   
            cal.setTime(oldNow);   
            cal.add(Calendar.HOUR, 8);// 24小时制   
            Date oldEt = cal.getTime();
            String newNow = format.format(oldEt).substring(0, 10);
            Date et = sdf.parse(newNow);
            
            if(!bt.before(et)) {
            	log.info("该能量球不能被采集");
            	return null;
            }
            //改变被取走的能量球的状态
            activityMapper.updatePointStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
            //改变被取走的计步球的状态
            walkMapper.updateStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
            //在交易记录表中增加取走的球
            this.addPointTradeRecord(userUuid, energyBallUuid, activityMapper.selectByUuid(energyBallUuid).getPoint());
            //更新用户能量钱包
            activityMapper.increasePoint(userUuid, activityMapper.selectByUuid(energyBallUuid).getPoint(), now);
            
            EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
            energyBallTakenResult.setNewEnergyPonit(activityMapper.selectByUuid(energyBallUuid).getPoint());
            energyBallTakenResult.setNewPower(BigDecimal.ZERO);
    		return energyBallTakenResult;
        }
        
		
	}
	

}
