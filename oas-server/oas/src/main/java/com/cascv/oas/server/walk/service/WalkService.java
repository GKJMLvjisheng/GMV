package com.cascv.oas.server.walk.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * 得到增加的point
	 * @param stepNum 
     * @return
     */
	public BigDecimal getPoint(BigDecimal stepNum) {
		ActivityRewardConfig activityRewardConfig = activityMapper.selectBaseValueBySourceCodeAndRewardCode(SOURCE_CODE_OF_WALKING, REWARD_CODE_OF_WALKING_POINT);
		BigDecimal point = activityRewardConfig.getIncreaseSpeed().multiply(stepNum);
		BigDecimal maxValue = activityRewardConfig.getMaxValue();
		BigDecimal newPoint;
		if(point.compareTo(maxValue) == -1)
			newPoint = point;
		else
			newPoint = maxValue;
		return newPoint;		
	}
	
	/**
     * 记录行走步数
     * @param stepNum
     * @return
     */
	public void addWalkPointBall(String userUuid, BigDecimal stepNum) {
		 String now = DateUtils.YYYY_MM_DD_HH_MM_SS;
		 walkBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
		 log.info("uuid={}", walkBall.getUuid());
		 walkBall.setUserUuid(userUuid);
		 walkBall.setStepNum(stepNum);
		 walkBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		 walkBall.setCreated(now);
		 walkBall.setUpdated(now);
		 walkMapper.insertWalkBall(walkBall);
		
	}
	
	/**
     * 记录行走能量球
     * @param stepNum userUuid
     * @return
     */
	public void addEnergyPointBall(String userUuid, BigDecimal stepNum) {		
		energyPointBall.setUuid(walkBall.getUuid());
		log.info("uuid={}", energyPointBall.getUuid());
		energyPointBall.setUserUuid(userUuid);
		energyPointBall.setSourceCode(SOURCE_CODE_OF_WALKING);
		energyPointBall.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		
		energyPointBall.setPoint(this.getPoint(stepNum));
		
		energyPointBall.setCreated(walkBall.getCreated());
		energyPointBall.setUpdated(walkBall.getUpdated());
		
		activityMapper.insertEnergyPointBall(energyPointBall);
		
	}
	
	/**
     * 插入行走能量球记录
	 * @param userUuid 
	 * @param stepNum 
     * @return
     */
	public void addPointTradeRecord(String userUuid, BigDecimal stepNum) {
		
		pointTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
		pointTradeRecord.setUserUuid(userUuid);
		pointTradeRecord.setInOrOut(ENERGY_IN);
		pointTradeRecord.setEnergyBallUuid(energyPointBall.getUuid());
		pointTradeRecord.setStatus(STATUS_OF_ACTIVE_ENERGYBALL);
		pointTradeRecord.setPointChange(this.getPoint(stepNum));
		
		String now = DateUtils.YYYY_MM_DD_HH_MM_SS;
		pointTradeRecord.setCreated(now);
		
	}
	
	/**
     * 查询行走能量球
     * @param userUuid
     * @return
     */
	public WalkBallReturn inquireWalkPointBall(String userUuid, BigDecimal stepNum) {
		if (StringUtils.isEmpty(userUuid)) {
            return null;
        }
		WalkBall walkBall = walkMapper
				.selectWalkBall(userUuid, STATUS_OF_ACTIVE_ENERGYBALL);
		//如果没有球，则产生球
		if(walkBall == null) {
			this.addWalkPointBall(userUuid, stepNum);
			this.addEnergyPointBall(userUuid, stepNum);
		}
		WalkBallReturn walkBallReturn = walkMapper.selectEnergyBallList(userUuid, SOURCE_CODE_OF_WALKING, STATUS_OF_ACTIVE_ENERGYBALL);
		return walkBallReturn;
		
	}
	
	/**
     * 采集行走能量球
	 * @param userUuid 
	 * @param energyBallUuid 
	 * @param stepNum 
     * @return
	 * @throws ParseException 
     */
	public EnergyBallTakenResult takeWalkPointBall(String userUuid, String energyBallUuid, BigDecimal stepNum) throws ParseException {
		if (StringUtils.isEmpty(userUuid) || StringUtils.isEmpty(energyBallUuid)) {
            log.info("userUuid or energyBallUuid is null");
            return null;
        }
        if (activityMapper.selectByUuid(energyBallUuid).getStatus().equals(STATUS_OF_DIE_ENERGYBALL)) {
            log.info("该能量球已经被获取！");
            return null;
        }
        String now = DateUtils.YYYY_MM_DD_HH_MM_SS;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date bt = sdf.parse(activityMapper.selectByUuid(energyBallUuid).getCreated());
        Date et = sdf.parse(now);
        if(!bt.before(et)) {
        	log.info("该能量球不能被采集");
        	return null;
        }
        //改变被取走的能量球的状态
        activityMapper.updatePointStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
        //改变被取走的计步球的状态
        walkMapper.updateStatusByUuid(energyBallUuid, STATUS_OF_DIE_ENERGYBALL, now);
        //在交易记录表中增加取走的球
        this.addPointTradeRecord(userUuid, stepNum);
        //更新用户能量钱包
        activityMapper.increasePoint(userUuid, this.getPoint(stepNum), now);
        
        EnergyBallTakenResult energyBallTakenResult = new EnergyBallTakenResult();
        energyBallTakenResult.setNewEnergyPonit(this.getPoint(stepNum));
        energyBallTakenResult.setNewPower(BigDecimal.ZERO);
		return energyBallTakenResult;
		
	}
	

}
