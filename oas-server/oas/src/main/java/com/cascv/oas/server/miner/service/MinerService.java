package com.cascv.oas.server.miner.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.model.PurchaseRecord;
import com.cascv.oas.server.miner.wrapper.UserMinerWrapper;
import com.cascv.oas.server.timezone.service.TimeZoneService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MinerService {
	
	@Autowired
	private MinerMapper minerMapper;
	
	@Autowired
	private TimeZoneService timeZoneService;
	
	public List<UserMinerWrapper> getUserMiner(String userUuid){
		List<UserMinerWrapper> userMinerList = minerMapper.selectByuserUuid(userUuid);
		return userMinerList;	
	}
	
	public BigDecimal getMinerEfficiency(String userUuid) {
		List<UserMinerWrapper> userMinerList = this.getUserMiner(userUuid);
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
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD);
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
		purchaseRecord.setMinerDescription(minerModel.getMinerDescription());
		purchaseRecord.setCreated(now);
		return minerMapper.insertPurchaseRecord(purchaseRecord);
		
	}

}
