package com.cascv.oas.server.activity.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.ActivityRewardConfig;
import com.cascv.oas.server.activity.model.EnergyPointBall;
import com.cascv.oas.server.activity.model.EnergyPowerBall;
import com.cascv.oas.server.activity.model.PointTradeRecord;
import com.cascv.oas.server.activity.model.PowerTradeRecord;
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
	
	private EnergyPointBall addEnergyPointBall = new EnergyPointBall();
	private EnergyPowerBall addEnergyPowerBall = new EnergyPowerBall();
	private PointTradeRecord addPointTradeRecord = new PointTradeRecord();
	private PowerTradeRecord addPowerTradeRecord = new PowerTradeRecord();
	private ActivityCompletionStatus addActivityCompletionStatus = new ActivityCompletionStatus();
	
	/**
     * 查询某奖励活动是否已存在
     * @param sourceCode
	 * @param rewardCode 
     * @return
     */
	public ActivityRewardConfig inquireRewardByRewardCode(Integer sourceCode, Integer rewardCode) {
		ActivityRewardConfig activityRewardConfig = activityMapper.selectMaxValueBySourceCodeAndrewardCode(sourceCode, rewardCode);
		return activityRewardConfig;
		
	}
	
	
	/**
     * 得到增加的point和power
     * @param sourceCode
	 * @param rewardCode 
     * @return
     */
	public EnergyResult getNewEnergy(Integer sourceCode, Integer rewardCode) {
		BigDecimal point = activityMapper.selectMaxValueBySourceCodeAndrewardCode(sourceCode, rewardCode).getMaxValue();
		BigDecimal power = activityMapper.selectMaxValueBySourceCodeAndrewardCode(sourceCode, rewardCode).getMaxValue();
		EnergyResult energyResult = new EnergyResult();
		energyResult.setNewPoint(point);
		energyResult.setNewPower(power);
		return energyResult;
		
	}
	
	/**
     * 产生新的积分球
	 * @param rewardCode 
     * @param userUuid, sourceCode
     * @return
     */
	public EnergyPointBall getEnergyPointBall(String userUuid, Integer sourceCode, Integer rewardCode) {
		addEnergyPointBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
		addEnergyPointBall.setUserUuid(userUuid);
		addEnergyPointBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		addEnergyPointBall.setSourceCode(sourceCode);			
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addEnergyPointBall.setCreated(now);
		
		EnergyResult energyResult = this.getNewEnergy(sourceCode, rewardCode);
		if(energyResult != null) {
			addEnergyPointBall.setPoint(energyResult.getNewPoint());
			return addEnergyPointBall;
		}else
			return null;
	}
	
	/**
     * 产生新的算力球
	 * @param rewardCode 
     * @param userUuid, sourceCode
     * @return
     */
	public EnergyPowerBall getEnergyPowerBall(String userUuid, Integer sourceCode, Integer rewardCode) {
		addEnergyPowerBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
		addEnergyPowerBall.setUserUuid(userUuid);
		addEnergyPowerBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		addEnergyPowerBall.setSourceCode(sourceCode);			
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addEnergyPowerBall.setCreated(now);
		
		EnergyResult energyResult = this.getNewEnergy(sourceCode, rewardCode);
		if(energyResult != null) {
			addEnergyPowerBall.setPower(energyResult.getNewPoint());
			return addEnergyPowerBall;
		}
		else
			return null;
		
	}
	
	/**
       * 产生新的积分记录
	 * @param sourceCode 
	 * @param rewardCode 
     * @param userUuid
     * @return
     */
	public PointTradeRecord getPointTradeRecord(String userUuid, Integer sourceCode, Integer rewardCode) {
		addPointTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
		addPointTradeRecord.setUserUuid(userUuid);
		addPointTradeRecord.setInOrOut(ENEGY_IN);
		addPointTradeRecord.setPointChange(this.getNewEnergy(sourceCode, rewardCode).getNewPoint());
		addPointTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYRECORD);	
		
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addPointTradeRecord.setCreated(now);
		
		EnergyPointBall energyPointBall = this.getEnergyPointBall(userUuid, sourceCode, rewardCode);
		if (energyPointBall != null) {
			addPointTradeRecord.setEnergyBalluuid(energyPointBall.getUuid());
			return addPointTradeRecord;
		}else {
			return null;			
		}		
		
	}
	
	/**
     * 产生新的算力记录
	 * @param sourceCode 
	 * @param rewardCode 
   * @param userUuid
   * @return
   */
	public PowerTradeRecord getPowerTradeRecord(String userUuid, Integer sourceCode, Integer rewardCode) {
		addPowerTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
		addPowerTradeRecord.setUserUuid(userUuid);
		addPowerTradeRecord.setInOrOut(ENEGY_IN);
		addPowerTradeRecord.setPowerChange(this.getNewEnergy(sourceCode, rewardCode).getNewPower());
		addPowerTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYRECORD);
		
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addPowerTradeRecord.setCreated(now);
		
		EnergyPowerBall energyPowerBall = this.getEnergyPowerBall(userUuid, sourceCode, rewardCode);
		if (energyPowerBall != null) {
			addPowerTradeRecord.setEnergyBalluuid(energyPowerBall.getUuid());
			return addPowerTradeRecord;
		}else
			return null;		
		
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
     * 奖励接口，往energy_point_ball中添加数据
	 * @param rewardCode 
     * @param userUuid, sourceCode
     * @return
     */
	public Integer addEnergyPointBall(String userUuid, Integer sourceCode, Integer rewardCode) {
		if(this.getEnergyPointBall(userUuid, sourceCode, rewardCode) != null)
			return activityMapper.insertEnergyPointBall(addEnergyPointBall);
		else
			return null;		
	}
	
	/**
     * 奖励接口，往energy_power_ball中添加数据
	 * @param rewardCode 
     * @param userUuid, sourceCode
     * @return
     */
	public Integer addEnergyPowerBall(String userUuid, Integer sourceCode, Integer rewardCode) {
		if(this.getEnergyPowerBall(userUuid, sourceCode, rewardCode) != null)
			return activityMapper.insertEnergyPowerBall(addEnergyPowerBall);
		else
			return null;
	}
	
	/**
     * 奖励接口，往point_trade_record中添加数据
	 * @param sourceCode 
	 * @param rewardCode 
     * @param userUuid
     * @return
     */
	public Integer addPointTradeRecord(String userUuid, Integer sourceCode, Integer rewardCode) {
		if (this.getPointTradeRecord(userUuid, sourceCode, rewardCode) != null)
			return activityMapper.insertPointTradeRecord(addPointTradeRecord);
		else
			return null;
	}
	
	/**
     * 奖励接口，往power_trade_record中添加数据
	 * @param sourceCode 
	 * @param rewardCode 
     * @param userUuid
     * @return
     */
	public Integer addPowerTradeRecord(String userUuid, Integer sourceCode, Integer rewardCode) {
		if (this.getPowerTradeRecord(userUuid, sourceCode, rewardCode) != null)		
			return activityMapper.insertPowerTradeRecord(addPowerTradeRecord);
		else
			return null;
	}
	
	/**
     * 更新能量钱包
	 * @param sourceCode 
     * @param userUuid
	 * @param rewardCode 
     * @return
     */
	public void updateEnergyWallet(String userUuid, Integer sourceCode, Integer rewardCode) {
		activityMapper.increasePoint(userUuid, this.getNewEnergy(sourceCode, rewardCode).getNewPoint());
		activityMapper.increasePower(userUuid, this.getNewEnergy(sourceCode, rewardCode).getNewPower());	
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
