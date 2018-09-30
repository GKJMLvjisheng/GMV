package com.cascv.oas.server.activity.service;

import java.math.BigDecimal;
import java.util.List;

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
import com.cascv.oas.server.activity.wrapper.EnergyResultPoint;
import com.cascv.oas.server.activity.wrapper.EnergyResultPower;
import com.cascv.oas.server.activity.wrapper.RewardConfigResult;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.model.ActivityCompletionStatus;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActivityService {
	
	@Autowired
	private ActivityMapper activityMapper;
	
	private static final Integer STATUS_OF_ACTIVE_ENERGYBALL = 1;       // 能量球活跃状态，可被获取
    private static final Integer STATUS_OF_DIE_ENERGYBALL = 0;          // 能量球死亡状态，不可被获取
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
     * 得到增加的point
     * @param sourceCode
	 * @param rewardCode 
     * @return
     */
	public EnergyResultPoint getNewPoint(Integer sourceCode, Integer rewardCode) {
		if(rewardCode == 1) {
			BigDecimal point = activityMapper.selectMaxValueBySourceCodeAndrewardCode(sourceCode, rewardCode).getMaxValue();
			EnergyResultPoint energyResultPoint = new EnergyResultPoint();
			energyResultPoint.setNewPoint(point);
			return energyResultPoint;
		}else 
			return null;		
	}
	
	/**
     * 得到增加的power
     * @param sourceCode
	 * @param rewardCode 
     * @return
     */
	public EnergyResultPower getNewPower(Integer sourceCode, Integer rewardCode) {
		if (rewardCode == 2) {
			BigDecimal power = activityMapper.selectMaxValueBySourceCodeAndrewardCode(sourceCode, rewardCode).getMaxValue();
			EnergyResultPower energyResultPower = new EnergyResultPower();
			energyResultPower.setNewPower(power);
			return energyResultPower;
		}else
			return null;				
	}
	
	/**
     * 产生新的积分球
	 * @param rewardCode 
     * @param userUuid, sourceCode
     * @return
     */
	public EnergyPointBall getEnergyPointBall(String userUuid, Integer sourceCode, Integer rewardCode) {
		addEnergyPointBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT_BALL));
		log.info("uuid={}",addEnergyPointBall.getUuid());
		addEnergyPointBall.setUserUuid(userUuid);
		addEnergyPointBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		addEnergyPointBall.setSourceCode(sourceCode);			
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addEnergyPointBall.setCreated(now);
		addEnergyPointBall.setUpdated(now);
		
		EnergyResultPoint energyResult = this.getNewPoint(sourceCode, rewardCode);
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
		addEnergyPowerBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POWER_BALL));
		log.info("uuid={}",addEnergyPowerBall.getUuid());
		addEnergyPowerBall.setUserUuid(userUuid);
		addEnergyPowerBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		addEnergyPowerBall.setSourceCode(sourceCode);			
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addEnergyPowerBall.setCreated(now);
		addEnergyPowerBall.setUpdated(now);
		
		EnergyResultPower energyResult = this.getNewPower(sourceCode, rewardCode);
		if(energyResult != null) {
			addEnergyPowerBall.setPower(energyResult.getNewPower());
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
		addPointTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT_RECORD));
		addPointTradeRecord.setUserUuid(userUuid);
		addPointTradeRecord.setInOrOut(ENEGY_IN);
		addPointTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYRECORD);	
		
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addPointTradeRecord.setCreated(now);
		
		if(this.getNewPoint(sourceCode, rewardCode) == null)
			addPointTradeRecord.setPointChange(BigDecimal.ZERO);
		else
			addPointTradeRecord.setPointChange(this.getNewPoint(sourceCode, rewardCode).getNewPoint());
		
		EnergyPointBall energyPointBall = addEnergyPointBall;
		if (energyPointBall != null) {
			addPointTradeRecord.setEnergyBallUuid(energyPointBall.getUuid());
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
		addPowerTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POWER_RECORD));
		addPowerTradeRecord.setUserUuid(userUuid);
		addPowerTradeRecord.setInOrOut(ENEGY_IN);
		addPowerTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYRECORD);
		
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		addPowerTradeRecord.setCreated(now);

		if(this.getNewPower(sourceCode, rewardCode) == null)
			addPowerTradeRecord.setPowerChange(BigDecimal.ZERO);
		else
			addPowerTradeRecord.setPowerChange(this.getNewPower(sourceCode, rewardCode).getNewPower());
		
		EnergyPowerBall energyPowerBall = addEnergyPowerBall;
		if (energyPowerBall != null) {
			addPowerTradeRecord.setEnergyBallUuid(energyPowerBall.getUuid());
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
		log.info("addPointTradeRecord={}",addPointTradeRecord.getEnergyBallUuid());
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
		BigDecimal valuePoint;
		BigDecimal valuePower;
		if(this.getNewPoint(sourceCode, rewardCode) != null && this.getNewPower(sourceCode, rewardCode) != null) {
			valuePoint = this.getNewPoint(sourceCode, rewardCode).getNewPoint();
			valuePower = this.getNewPower(sourceCode, rewardCode).getNewPower();
			activityMapper.increasePoint(userUuid, valuePoint);
			activityMapper.increasePower(userUuid, valuePower);	
		}else if (this.getNewPoint(sourceCode, rewardCode) != null && this.getNewPower(sourceCode, rewardCode) == null){
			valuePoint = this.getNewPoint(sourceCode, rewardCode).getNewPoint();
			valuePower = BigDecimal.ZERO;
			activityMapper.increasePoint(userUuid, valuePoint);
			activityMapper.increasePower(userUuid, valuePower);
		}else if(this.getNewPoint(sourceCode, rewardCode) == null && this.getNewPower(sourceCode, rewardCode) != null) {
			valuePoint = BigDecimal.ZERO;
			valuePower = this.getNewPower(sourceCode, rewardCode).getNewPower();
			activityMapper.increasePoint(userUuid, valuePoint);
			activityMapper.increasePower(userUuid, valuePower);
		}else if(this.getNewPoint(sourceCode, rewardCode) == null && this.getNewPower(sourceCode, rewardCode) == null){
			valuePoint = BigDecimal.ZERO;
			valuePower = BigDecimal.ZERO;
			activityMapper.increasePoint(userUuid, valuePoint);
			activityMapper.increasePower(userUuid, valuePower);
		}
			
		
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
	
	/**
     * 获得奖励
	 * @param sourceCode 
     * @param userUuid
     * @return
     */
	
	 public void getReward(Integer sourceCode, String userUuid){
			List<RewardConfigResult> activityRewardConfigList = activityMapper.selectActivityRewardBySourceCode(sourceCode);
			Integer len = activityRewardConfigList.size();
			log.info("len={}",len);
			for(int i=0; i<len; i++) {
				Integer rewardCode = activityRewardConfigList.get(i).getRewardCode();
				log.info("rewardCode={}",rewardCode);
				this.addEnergyPointBall(userUuid, sourceCode, rewardCode);
				this.addEnergyPowerBall(userUuid, sourceCode, rewardCode);
				this.addPointTradeRecord(userUuid, sourceCode, rewardCode);
				this.addPowerTradeRecord(userUuid, sourceCode, rewardCode);
				this.updateEnergyWallet(userUuid, sourceCode, rewardCode);
				this.addActivityCompletionStatus(userUuid, sourceCode);
			}
			
		}
	 
	   /**
	     * 更新积分能量球状态
	     * @return
	     */
	    public int updateEnergyPointBallStatusByUuid(String userUuid) {
	        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
	        String uuid = addEnergyPointBall.getUuid();
	        return activityMapper.updatePointStatusByUuid(uuid, STATUS_OF_DIE_ENERGYBALL, now);
	    }
	    
	    /**
	     * 更新算力能量球状态
	     * @return
	     */
	    public int updateEnergyPowerBallStatusByUuid(String userUuid) {
	        String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
	        String uuid = addEnergyPowerBall.getUuid();
	        return activityMapper.updatePowerStatusByUuid(uuid, STATUS_OF_DIE_ENERGYBALL, now);
	    }

}
