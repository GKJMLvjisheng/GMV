package com.cascv.oas.server.miner.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.model.PurchaseRecord;
import com.cascv.oas.server.miner.wrapper.PurchaseRecordWrapper;
import com.cascv.oas.server.timezone.service.TimeZoneService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MinerService {
	
	@Autowired
	private MinerMapper minerMapper;
	
	@Autowired
	private TimeZoneService timeZoneService;
	
	private static final Integer STATUS_ACTIVITY_OF_MINER = 0;  //矿机处于工作状态
	
	//得到用户购买的矿机的全部信息
	public List<PurchaseRecord> getUserMiner(String userUuid){
		List<PurchaseRecord> userMinerList = minerMapper.selectByuserUuid(userUuid);
		return userMinerList;	
	}
	
	//得到用户通过购买矿机提升的算力
	public BigDecimal getPowerSum(String userUuid) {
		List<PurchaseRecord> userMinerList = this.getUserMiner(userUuid);
		BigDecimal powerSum = BigDecimal.ZERO;
		for(int i=0; i<userMinerList.size(); i++) {
			if (userMinerList.get(i).getMinerStatus() == 1) {
				Integer minerNum = userMinerList.get(i).getMinerNum();
				BigDecimal minerPower = userMinerList.get(i).getMinerPower();
				powerSum = powerSum.add(minerPower.multiply(BigDecimal.valueOf((int)minerNum)));
			}
		}
		log.info("efficiencySum={}", powerSum);
		return powerSum;
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
		purchaseRecord.setMinerCode(minerModel.getMinerCode());
		purchaseRecord.setMinerName(minerName);
		purchaseRecord.setMinerGrade(minerModel.getMinerGrade());
		purchaseRecord.setMinerPrice(minerModel.getMinerPrice());
		purchaseRecord.setMinerNum(minerNum);
		purchaseRecord.setPriceSum(priceSum);
		purchaseRecord.setMinerPower(minerModel.getMinerPower());
		purchaseRecord.setMinerPeriod(minerModel.getMinerPeriod());
		purchaseRecord.setMinerStatus(STATUS_ACTIVITY_OF_MINER);
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
			purchaseRecord.setPriceSum(purchaseRecordList.get(i).getPriceSum());
			purchaseRecord.setUserUuid(purchaseRecordList.get(i).getUserUuid());
			purchaseRecord.setUuid(purchaseRecordList.get(i).getUuid());
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
					minerMapper.updateStatusByUuid(purchaseRecordList.get(i).getUuid());
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
