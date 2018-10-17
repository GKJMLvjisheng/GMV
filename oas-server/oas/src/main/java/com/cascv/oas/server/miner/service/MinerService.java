package com.cascv.oas.server.miner.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
	
	public List<PurchaseRecord> getUserMiner(String userUuid){
		List<PurchaseRecord> userMinerList = minerMapper.selectByuserUuid(userUuid);
		return userMinerList;	
	}
	
	public BigDecimal getMinerEfficiency(String userUuid) {
		List<PurchaseRecord> userMinerList = this.getUserMiner(userUuid);
		BigDecimal powerSum = BigDecimal.ZERO;
		for(int i=0; i<userMinerList.size(); i++) {
			
		}
		log.info("efficiencySum={}", powerSum);
		return powerSum;
	}
	
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
	
	public List<PurchaseRecord> inquerePurchaseRecord(String userUuid, Integer offset, Integer limit){
		List<PurchaseRecord> purchaseRecordList = minerMapper.inquerePurchaseRecord(userUuid, offset, limit);
		for(PurchaseRecord purchaseRecord : purchaseRecordList) {
			String srcFormater="yyyy-MM-dd HH:mm:ss";
		    String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String created=DateUtils.string2Timezone(srcFormater, purchaseRecord.getCreated(), dstFormater, dstTimeZoneId);
			purchaseRecord.setCreated(created);
			log.info("created={}", created);
		}
		return purchaseRecordList;
	}
	
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
