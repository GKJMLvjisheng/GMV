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
	Integer updateStatusByUuid(@Param("uuid") String uuid);
	List<PurchaseRecord> selectByMinerPurchaseStatus();
	List<PurchaseRecord> inquerePurchaseRecord(@Param("userUuid") String userUuid, 
			@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer insertPurchaseRecord(PurchaseRecord purchaseRecord);
	Integer countByUserUuid(@Param("userUuid") String userUuid);

}
