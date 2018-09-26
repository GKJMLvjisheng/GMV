package com.cascv.oas.server.activity.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.wrapper.EnergyResult;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;
import com.cascv.oas.server.energy.model.EnergyBall;
import com.cascv.oas.server.energy.model.EnergyTradeRecord;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActivityService {
	
	@Autowired
	private ActivityMapper activityMapper;
	
	private static final Integer STATUS_OF_ACTIVE_ENERGYBALL = 1;       // 能量球活跃状态，可被获取
//    private static final Integer STATUS_OF_DIE_ENERGYBALL = 0;          // 能量球死亡状态，不可被获取
    private static final Integer ENEGY_IN = 1;               // 能量增加为1，能量减少为0
    private static final Integer STATUS_OF_ACTIVE_ENERGYRECORD = 1;    // 能量记录活跃状态，可被获取
//    private static final Integer STATUS_OF_DIE_ENERGYRECORD = 0;       // 能量记录活跃状态，不可被获取
    private static final Integer STATUS_OF_ACTIVITY = 1;             //表示已完成该任务
	
	private EnergyBall addEnergyBall = new EnergyBall();
	private EnergyTradeRecord addEnergyTradeRecord = new EnergyTradeRecord();
	private ActivityCompletionStatus addActivityCompletionStatus = new ActivityCompletionStatus();
	
	/**
     * 得到增加的point和power
     * @param sourceCode
     * @return
     */
	public EnergyResult getNewEnergy(Integer sourceCode) {
		BigDecimal point = activityMapper.selectActivityBySourceCode(sourceCode).getPointCapacityEachBall();
		BigDecimal power = activityMapper.selectActivityBySourceCode(sourceCode).getPowerCapacityEachBall();
		EnergyResult energyResult = new EnergyResult();
		energyResult.setNewPoint(point);
		energyResult.setNewPower(power);
		return energyResult;
		
	}
	
	/**
     * 产生新的能量球
     * @param userUuid, sourceCode
     * @return
     */
	public EnergyBall getEnergyBall(String userUuid, Integer sourceCode) {
		addEnergyBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
		addEnergyBall.setUserUuid(userUuid);
		addEnergyBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		addEnergyBall.setPointSource(sourceCode);
		addEnergyBall.setPowerSource(sourceCode);
		
		EnergyResult energyResult = this.getNewEnergy(sourceCode);
		addEnergyBall.setPoint(energyResult.getNewPoint());
		addEnergyBall.setPower(energyResult.getNewPower());
		
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addEnergyBall.setTimeCreated(now);
		addEnergyBall.setTimeUpdated(now);
		return addEnergyBall;
	}
	
	/**
       * 产生新的能量记录
	 * @param sourceCode 
     * @param userUuid, energyBallUuid
     * @return
     */
	public EnergyTradeRecord getEnergyTradeRecord(String userUuid, Integer sourceCode) {
		addEnergyTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
		addEnergyTradeRecord.setUserUuid(userUuid);
		
		EnergyBall energyBall = this.getEnergyBall(userUuid, sourceCode);
		addEnergyTradeRecord.setEnergyBallUuid(energyBall.getUuid());
		
		addEnergyTradeRecord.setInOrOut(ENEGY_IN);
		addEnergyTradeRecord.setPointChange(this.getNewEnergy(sourceCode).getNewPoint());
		addEnergyTradeRecord.setPowerChange(this.getNewEnergy(sourceCode).getNewPower());
		addEnergyTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYRECORD);
		
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addEnergyTradeRecord.setTimeCreated(now);
		addEnergyTradeRecord.setTimeUpdated(now);
		return addEnergyTradeRecord;
		
	}
	
	/**
     * 更新活动的完成情况
	 * @param sourceCode 
     * @param userUuid
     * @return
     */
	public ActivityCompletionStatus getActivityCompletionStatus(String userUuid, Integer sourceCode) {
		log.info("uuid{}",UuidUtils.getPrefixUUID(UuidPrefix.ACTIVITY_COMPLETION_STATUS));
		addActivityCompletionStatus.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ACTIVITY_COMPLETION_STATUS));
		addActivityCompletionStatus.setUserUuid(userUuid);
		addActivityCompletionStatus.setSourceCode(sourceCode);
		addActivityCompletionStatus.setStatus(STATUS_OF_ACTIVITY);
		
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addActivityCompletionStatus.setCreated(now);
		addActivityCompletionStatus.setUpdated(now);
		
		return addActivityCompletionStatus;		
	}
	
	/**
     * 奖励接口，往energy_ball中添加数据
     * @param userUuid, sourceCode
     * @return
     */
	public Integer addEnergyBall(String userUuid, Integer sourceCode) {
		this.getEnergyBall(userUuid, sourceCode);		
		return activityMapper.insertEnergyBall(addEnergyBall);
	}
	
	/**
     * 奖励接口，往energy_trade_record中添加数据
	 * @param sourceCode 
     * @param userUuid, energyBallUuid
     * @return
     */
	public Integer addEnergyTradeRecord(String userUuid, Integer sourceCode) {
		this.getEnergyTradeRecord(userUuid, sourceCode);		
		return activityMapper.insertEnergyTradeRecord(addEnergyTradeRecord);		
	}
	
	/**
     * 更新能量钱包
	 * @param sourceCode 
     * @param userUuid
     * @return
     */
	public void updateEnergyWallet(String userUuid, Integer sourceCode) {
		activityMapper.increasePoint(userUuid, this.getNewEnergy(sourceCode).getNewPoint());
		activityMapper.increasePower(userUuid, this.getNewEnergy(sourceCode).getNewPower());	
	}
	
	/**
     * 奖励接口，往activity_completion_status中添加数据
	 * @param sourceCode 
     * @param userUuid
     * @return
     */
	public Integer addActivityCompletionStatus(String userUuid, Integer sourceCode) {
		this.getActivityCompletionStatus(userUuid, sourceCode);
		return activityMapper.insertActivityCompletionStatus(addActivityCompletionStatus);
	}
	
	

}
