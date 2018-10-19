package com.cascv.oas.server.miner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.model.PurchaseRecord;


@Component
public interface MinerMapper {
	
	Integer insertMiner(MinerModel minerModel);
	Integer deleteMiner(@Param("minerCode") String minerCode);
	Integer updateMiner(MinerModel minerModel);
	
	MinerModel inquireByUuid(@Param("minerCode") String minerCode);
	MinerModel inquireByMinerName(@Param("minerName") String minerName);
	MinerModel inquireUpdateMinerName(@Param("minerName") String minerName, 
			@Param("updateMinerName") String updateMinerName);
	
	List<MinerModel> selectAllWebMiner();
	List<MinerModel> selectAllMiner(@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countNum();
	
	List<PurchaseRecord> selectAllRecord();
	List<PurchaseRecord> selectByuserUuid(@Param("userUuid") String userUuid);
	List<PurchaseRecord> selectByuserUuidMinerStatus(@Param("userUuid") String userUuid);
	List<String> selectUserUuidByMinerStatus();
	List<PurchaseRecord> selectByMinerStatus(@Param("userUuid") String userUuid);
	
	PurchaseRecord selectByUuid(@Param("uuid") String uuid);
	Integer updateStatusByUuid(@Param("uuid") String uuid);
	List<PurchaseRecord> selectByMinerStatusPowerRewardStatusToDecrease();
	List<PurchaseRecord> selectByMinerPurchaseStatus();
	List<PurchaseRecord> selectByMinerStatusPowerRewardStatus();
	Integer updateByRewardEnergyBallUuid(PurchaseRecord purchaseRecord);
	Integer updateByPowerRewardStatusRewardEnergyBallUuid(PurchaseRecord purchaseRecord);//撤出算力更新记录
	Integer updateByMinerPurchaseStatus(PurchaseRecord purchaseRecord);
	Integer updateByPowerRewardStatus(PurchaseRecord purchaseRecord);
	Integer updateByPowerRewardStatusToDecrease(PurchaseRecord purchaseRecord);
	List<PurchaseRecord> inquerePurchaseRecord(@Param("userUuid") String userUuid, 
			@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer insertPurchaseRecord(PurchaseRecord purchaseRecord);
	Integer countByUserUuid(@Param("userUuid") String userUuid);

}
