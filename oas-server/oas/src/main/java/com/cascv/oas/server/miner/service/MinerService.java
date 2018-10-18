package com.cascv.oas.server.miner.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.EnergyPowerBall;
import com.cascv.oas.server.activity.model.PowerTradeRecord;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.miner.job.MinerJob;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.model.PurchaseRecord;
import com.cascv.oas.server.miner.wrapper.PurchaseRecordWrapper;
import com.cascv.oas.server.scheduler.service.SchedulerService;
import com.cascv.oas.server.timezone.service.TimeZoneService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MinerService {
	
	@Autowired
	private MinerMapper minerMapper;
	
	@Autowired
	private TimeZoneService timeZoneService;
	
	@Autowired
	private ActivityMapper activityMapper;
	
	@Autowired
	private SchedulerService schedulerService;
	
	private static final Integer STATUS_ACTIVITY_OF_MINER = 1;  //矿机处于工作状态
	private static final Integer STATUS_DIE_OF_MINER = 1;  //矿机处于工作状态
	private static final Integer MINER_PURCHASE_STATUS = 0;  //矿机推广立即奖励未完成
	private static final Integer ACTIVITY_CODE_OF_MINER = 10;  //矿机处于工作状态
	private static final Integer ENEGY_IN = 1;               // 能量增加为1，能量减少为0
	private static final Integer ENEGY_OUT = 0;               // 能量增加为1，能量减少为0
	
	private EnergyPowerBall energyPowerBall = new EnergyPowerBall();
	
	//得到用户购买的矿机的全部信息
	public List<PurchaseRecord> getUserMiner(String userUuid){
		List<PurchaseRecord> userMinerList = minerMapper.selectByuserUuid(userUuid);
		return userMinerList;	
	}
	
	//一条算力记录的算力
	public BigDecimal getPowerSum(String uuid) {
		PurchaseRecord purchaseRecord = minerMapper.selectByUuid(uuid);
		BigDecimal powerSum = BigDecimal.ZERO;
		Integer minerNum = purchaseRecord.getMinerNum();
		BigDecimal minerPower = purchaseRecord.getMinerPower();
		powerSum = minerPower.multiply(BigDecimal.valueOf((int)minerNum));			
		return powerSum;
	}
	
	//增加算力球
	public void addMinerPowerBall(String userUuid, BigDecimal powerSum) {
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		energyPowerBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
		energyPowerBall.setSourceCode(ACTIVITY_CODE_OF_MINER);
		energyPowerBall.setUserUuid(userUuid);
		energyPowerBall.setStatus(STATUS_ACTIVITY_OF_MINER);
		energyPowerBall.setPower(powerSum);
		energyPowerBall.setCreated(now);
		energyPowerBall.setUpdated(now);
		activityMapper.insertEnergyPowerBall(energyPowerBall);
	}
	
	//往power_trade_recocrd插入算力增加的记录
	public void addMinerPowerTradeRecord(String userUuid, BigDecimal powerSum) {
		PowerTradeRecord powerTradeRecord = new PowerTradeRecord();
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		powerTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
		powerTradeRecord.setUserUuid(userUuid);
		powerTradeRecord.setEnergyBallUuid(energyPowerBall.getUuid());
		powerTradeRecord.setInOrOut(ENEGY_IN);
		powerTradeRecord.setPowerChange(powerSum);
		powerTradeRecord.setCreated(now);
		powerTradeRecord.setStatus(STATUS_ACTIVITY_OF_MINER);
		activityMapper.insertPowerTradeRecord(powerTradeRecord);
	}
	
	//往power_trade_recocrd插入算力减少的记录
	public void subMinerPowerTradeRecord(String userUuid, String energyBallUuid, String uuid) {
		PowerTradeRecord powerTradeRecord = new PowerTradeRecord();
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		powerTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
		powerTradeRecord.setUserUuid(userUuid);
		powerTradeRecord.setEnergyBallUuid(energyBallUuid);
		powerTradeRecord.setPowerChange(this.getPowerSum(uuid));
		powerTradeRecord.setInOrOut(ENEGY_OUT);
		powerTradeRecord.setStatus(STATUS_DIE_OF_MINER);
		powerTradeRecord.setCreated(now);
		activityMapper.insertPowerTradeRecord(powerTradeRecord);
	}

	
	//安卓前端显示目前可给购买的矿机的信息
	public List<MinerModel> selectAllMiner(Integer offset, Integer limit){
		List<MinerModel> minerModelList = minerMapper.selectAllMiner(offset, limit);
		for(MinerModel minerModel : minerModelList) {
			String srcFormater="yyyy-MM-dd HH:mm:ss";
		    String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String updated=DateUtils.string2Timezone(srcFormater, minerModel.getUpdated(), dstFormater, dstTimeZoneId);
		    minerModel.setUpdated(updated);
			log.info("updated={}", minerModel.getUpdated());
		}
		return minerModelList;
	}
	
	//用户购买矿机，并将其记录到购买详情表中
	public Integer addPurchaseRecord(String userUuid, String minerName, Integer minerNum, BigDecimal priceSum) {
		MinerModel minerModel = minerMapper.inquireByMinerName(minerName);
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		PurchaseRecord purchaseRecord = new PurchaseRecord();
		purchaseRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.PURCHASE_RECORD));
		purchaseRecord.setUserUuid(userUuid);
		purchaseRecord.setEnergyBallUuid(energyPowerBall.getUuid());
		purchaseRecord.setMinerCode(minerModel.getMinerCode());
		purchaseRecord.setMinerName(minerName);
		purchaseRecord.setMinerGrade(minerModel.getMinerGrade());
		purchaseRecord.setMinerPrice(minerModel.getMinerPrice());
		purchaseRecord.setMinerNum(minerNum);
		purchaseRecord.setPriceSum(priceSum);
		purchaseRecord.setMinerPower(minerModel.getMinerPower());
		purchaseRecord.setMinerPeriod(minerModel.getMinerPeriod());
		purchaseRecord.setMinerStatus(STATUS_ACTIVITY_OF_MINER);
		purchaseRecord.setMinerPurchaseStatus(MINER_PURCHASE_STATUS);
		purchaseRecord.setMinerDescription(minerModel.getMinerDescription());
		purchaseRecord.setCreated(now);
		return minerMapper.insertPurchaseRecord(purchaseRecord);
		
	}
	
	//用户购买记录查询
	public List<PurchaseRecordWrapper> inquerePurchaseRecord(String userUuid, Integer offset, Integer limit){
		List<PurchaseRecord> purchaseRecordList = minerMapper.inquerePurchaseRecord(userUuid, offset, limit);
		List<PurchaseRecordWrapper> purchaseRecordWrapper = new ArrayList<>();
		for(int i=0; i<purchaseRecordList.size(); i++) {
			PurchaseRecordWrapper purchaseRecord = new PurchaseRecordWrapper();
			purchaseRecord.setMinerCode(purchaseRecordList.get(i).getMinerCode());
			purchaseRecord.setMinerDescription(purchaseRecordList.get(i).getMinerDescription());
			purchaseRecord.setMinerGrade(purchaseRecordList.get(i).getMinerGrade());
			purchaseRecord.setMinerName(purchaseRecordList.get(i).getMinerName());
			purchaseRecord.setMinerNum(purchaseRecordList.get(i).getMinerNum());
			purchaseRecord.setMinerPower(purchaseRecordList.get(i).getMinerPower());
			purchaseRecord.setMinerPrice(purchaseRecordList.get(i).getMinerPrice());
			purchaseRecord.setMinerStatus(purchaseRecordList.get(i).getMinerStatus());
			purchaseRecord.setMinerPurchaseStatus(purchaseRecordList.get(i).getMinerPurchaseStatus());
			purchaseRecord.setPriceSum(purchaseRecordList.get(i).getPriceSum());
			purchaseRecord.setUserUuid(purchaseRecordList.get(i).getUserUuid());
			purchaseRecord.setUuid(purchaseRecordList.get(i).getUuid());
			purchaseRecord.setEnergyBallUuid(purchaseRecordList.get(i).getEnergyBallUuid());
			String srcFormater="yyyy-MM-dd HH:mm:ss";
		    String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String created=DateUtils.string2Timezone(srcFormater, purchaseRecordList.get(i).getCreated(), dstFormater, dstTimeZoneId);
			purchaseRecord.setCreated(created);
			Integer period = purchaseRecordList.get(i).getMinerPeriod();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				Date bt = sdf.parse(purchaseRecordList.get(i).getCreated());
				Calendar beginTime = Calendar.getInstance();
				beginTime.setTime(bt);
				beginTime.add(Calendar.DAY_OF_YEAR, period);
				Date et = beginTime.getTime();
				String endTime = sdf.format(et);
				String newEndTime = DateUtils.string2Timezone(srcFormater, endTime, dstFormater, dstTimeZoneId);
				purchaseRecord.setMinerEndTime(newEndTime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			purchaseRecordWrapper.add(purchaseRecord);
		}
		return purchaseRecordWrapper;
	}
	
	//实时查询用户购买的矿机的生命周期
	public synchronized void updateMinerStatus() {
		log.info("check status ...");
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		List<PurchaseRecord> purchaseRecordList = minerMapper.selectAllRecord();
		for(int i=0; i<purchaseRecordList.size(); i++) {
			String created = purchaseRecordList.get(i).getCreated();
			Integer period = purchaseRecordList.get(i).getMinerPeriod();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			log.info("sdf={}",sdf);
			try {
				log.info("created={}", created);
				Date bt = sdf.parse(created);
				log.info("bt={}",bt);
				Date et = sdf.parse(now);
				log.info("et={}",et);
				Calendar beginTime = Calendar.getInstance();
				beginTime.setTime(bt);
				beginTime.add(Calendar.DAY_OF_YEAR, period);
				Date endTime = beginTime.getTime();
				log.info("endTime={}",endTime);
				if(et.before(endTime)) {
					log.info("矿机工作中");
				}else {
					String uuid = purchaseRecordList.get(i).getUuid();
					String userUuid = purchaseRecordList.get(i).getUserUuid();
					String energyBallUuid = purchaseRecordList.get(i).getEnergyBallUuid();
					//更新购买记录的状态(即更新矿机的状态)
					minerMapper.updateStatusByUuid(uuid);
					//更新算力球状态
					activityMapper.updatePowerStatusByUuid(energyBallUuid, STATUS_DIE_OF_MINER, now);
					//更新能量钱包
					activityMapper.decreasePower(userUuid, this.getPowerSum(uuid), now);
					//增加算力减少的记录
					this.subMinerPowerTradeRecord(userUuid, energyBallUuid, uuid);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@PostConstruct
	  public void startMinerJob() {
	    JobDetail jobDetail = JobBuilder.newJob(MinerJob.class)
	        .withIdentity("JobDetailB", "groupB").build();
	    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("triggerB", "groupB")
	        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
	            .withIntervalInSeconds(28800).repeatForever()).startNow().build();
	    jobDetail.getJobDataMap().put("minerService", this);
	    schedulerService.addJob(jobDetail, trigger);
	    log.info("check status of miner ...");
	  }

}
