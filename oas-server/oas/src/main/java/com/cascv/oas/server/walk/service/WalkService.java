package com.cascv.oas.server.walk.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.cascv.oas.server.energy.vo.EnergyBallTakenResult;
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
	
	private static final Integer SOURCE_CODE_OF_WALKING = 9;             // 能量球来源：计步为9
	private static final Integer REWARD_CODE_OF_WALKING_POINT = 1;             //能量球奖励：计步奖励积分，为1
	private static final Integer STATUS_OF_ACTIVE_ENERGYBALL = 1;         //能量球活跃状态，可被获取
	private static final Integer STATUS_OF_DIE_ENERGYBALL = 0;            //能量球死亡状态，不可被获取
	private static final Integer ENERGY_IN = 1;                            //表示能量球的进账
	
	private EnergyPointBall energyPointBall = new EnergyPointBall();
	private PointTradeRecord pointTradeRecord = new PointTradeRecord();
	private WalkBall walkBall = new WalkBall();
	
	/**
     * 得到日期对应的增加的point
	 * @param stepNum 
     * @return
     */
	public List<StepPointQuota> getPoint(List<StepNumQuota> quota) {
		ActivityRewardConfig activityRewardConfig = activityMapper.selectBaseValueBySourceCodeAndRewardCode(SOURCE_CODE_OF_WALKING, REWARD_CODE_OF_WALKING_POINT);
		List<StepPointQuota> stepPointQuotaList = new ArrayList<>();
		for(int i=0; i<quota.size(); i++){
			StepPointQuota stepPointQuota = new StepPointQuota();
			BigDecimal stepNum = quota.get(i).getStepNum();
			BigDecimal point = activityRewardConfig.getIncreaseSpeed().multiply(stepNum);
			BigDecimal maxValue = activityRewardConfig.getMaxValue();
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
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
		 oneWalkBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
		 log.info("uuid={}", oneWalkBall.getUuid());
		 oneWalkBall.setUserUuid(userUuid);
		 oneWalkBall.setStepNum(stepNumQuota.getStepNum());
		 oneWalkBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		 oneWalkBall.setCreated(stepNumQuota.getDate());
		 oneWalkBall.setUpdated(now);
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
			String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
			 walkBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
			 log.info("uuid={}", walkBall.getUuid());
			 walkBall.setUserUuid(userUuid);
			 walkBall.setStepNum(quota.get(i).getStepNum());
			 walkBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
			 walkBall.setCreated(quota.get(i).getDate());
			 walkBall.setUpdated(now);
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
		newEnergyPointBall.setSourceCode(SOURCE_CODE_OF_WALKING);
		newEnergyPointBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		
		List<StepNumQuota> stepNumQuotaList = new ArrayList<>();
		stepNumQuotaList.add(stepNumQuota);
		newEnergyPointBall.setPoint(this.getPoint(stepNumQuotaList).get(0).getPoint());
		
		newEnergyPointBall.setCreated(this.getPoint(stepNumQuotaList).get(0).getDate());
		newEnergyPointBall.setUpdated(this.getPoint(stepNumQuotaList).get(0).getDate());
		
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
			energyPointBall.setSourceCode(SOURCE_CODE_OF_WALKING);
			energyPointBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
			
			energyPointBall.setPoint(this.getPoint(quota).get(i).getPoint());
			
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
		List<WalkBall> walkBallList = walkMapper
				.selectWalkBall(userUuid, STATUS_OF_ACTIVE_ENERGYBALL);
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
					String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
					if(quota.get(i).getDate().equals(now)) {
							walkMapper.updateStepNumByCreated(userUuid, quota.get(i).getStepNum(), quota.get(i).getDate());
							List<StepNumQuota> stepNumQuotaList = new ArrayList<>();
							stepNumQuotaList.add(quota.get(i));
							walkMapper.updatePointByuuid(walkBallMap.get(now).getUuid(), this.getPoint(stepNumQuotaList).get(0).getPoint());
							walkBallMap.get(now).setStepNum(quota.get(i).getStepNum());
							walkBallMap.put(now, walkBallMap.get(now));
					}
				}
			}
		}
		List<WalkBallReturn> walkBallReturnList = walkMapper.selectEnergyBallList(userUuid, SOURCE_CODE_OF_WALKING, STATUS_OF_ACTIVE_ENERGYBALL);
		return walkBallReturnList;
		
	}
	
	/**
     * 采集行走能量球
	 * @param userUuid 
	 * @param energyBallUuid 
	 * @param stepNum 
     * @return
	 * @throws ParseException 
     */
	public EnergyBallTakenResult takeWalkPointBall(String userUuid, String energyBallUuid) throws ParseException {
		if (StringUtils.isEmpty(userUuid) || StringUtils.isEmpty(energyBallUuid)) {
            log.info("userUuid or energyBallUuid is null");
            return null;
        }
        if (activityMapper.selectByUuid(energyBallUuid).getStatus().equals(STATUS_OF_DIE_ENERGYBALL)) {
            log.info("该能量球已经被获取！");
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
        Date bt = formatter.parse(activityMapper.selectByUuid(energyBallUuid).getCreated());
        Date et = formatter.parse(now);
        System.out.println(et);
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
