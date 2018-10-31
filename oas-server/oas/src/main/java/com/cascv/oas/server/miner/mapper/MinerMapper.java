package com.cascv.oas.server.miner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.model.PurchaseRecord;
import com.cascv.oas.server.miner.model.SystemParameterModel;
import com.cascv.oas.server.miner.wrapper.AccountTypeMiner;
import com.cascv.oas.server.miner.wrapper.UserPurchaseRecord;


@Component
public interface MinerMapper {
	
	Integer insertMiner(MinerModel minerModel);
	Integer deleteMiner(@Param("minerCode") String minerCode);
	Integer updateMiner(MinerModel minerModel);
	Integer updateOrderNum(@Param("minerCode") String minerCode,
			@Param("orderNum") Integer orderNum,
			@Param("updated") String updated);
	
	MinerModel inquireByUuid(@Param("minerCode") String minerCode);
	MinerModel inquireByMinerName(@Param("minerName") String minerName);
	MinerModel inquireUpdateMinerName(@Param("minerName") String minerName, 
			@Param("updateMinerName") String updateMinerName);
	MinerModel inquireByOrderNum(@Param("orderNum") Integer orderNum);
	
	List<MinerModel> selectAllWebMiner();
	List<MinerModel> selectAllMiner(@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countNum();
	
	
	Integer insertSystemParameter(SystemParameterModel systemParameterModel);
	Integer deleteSystemParameterByUuid(@Param("uuid") Integer uuid);
	List<SystemParameterModel> selectSystemParameter();
	SystemParameterModel selectSystemParameterByCurrency(@Param("currency") Integer currency);
	Integer updateSystemParameterByUuid(SystemParameterModel systemParameterModel);
	
	List<PurchaseRecord> selectAllRecord();
	List<PurchaseRecord> selectByuserUuid(@Param("userUuid") String userUuid);
	List<PurchaseRecord> selectByuserUuidMinerStatus(@Param("userUuid") String userUuid);
	List<String> selectUserUuidByMinerStatus();
	List<PurchaseRecord> selectByMinerStatus(@Param("userUuid") String userUuid);
	
	Integer inquireSumMinerNum();
	List<AccountTypeMiner> inquireTypeMinerNum();
	
	List<UserPurchaseRecord> selectUserPurchaseRecord(@Param("searchValue") String searchValue,
			@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer countBySearchValue(@Param("searchValue") String searchValue);
	
	PurchaseRecord selectByUuid(@Param("uuid") String uuid);
	Integer updateStatusByUuid(@Param("uuid") String uuid);
	List<PurchaseRecord> selectByMinerStatusPowerRewardStatusToDecrease();
	List<PurchaseRecord> selectByMinerPurchaseStatus();
	List<PurchaseRecord> selectByMinerStatusPowerRewardStatus();
	Integer updateByRewardEnergyBallUuid(PurchaseRecord purchaseRecord);
	Integer updateByFinishRewardUpdated(PurchaseRecord purchaseRecord);
	Integer updateByFinishRewardNumber(PurchaseRecord purchaseRecord);
	Integer updateByPowerRewardStatusRewardEnergyBallUuid(PurchaseRecord purchaseRecord);//撤出算力更新记录
	Integer updateByMinerPurchaseStatus(PurchaseRecord purchaseRecord);
	Integer updateByPowerRewardStatus(PurchaseRecord purchaseRecord);
	Integer updateByPowerRewardStatusToDecrease(PurchaseRecord purchaseRecord);
	Integer updateByMinerNumFinishRewardNumber(PurchaseRecord purchaseRecord);//无奖励时候假装已经完成奖励
	List<PurchaseRecord> inquerePurchaseRecord(@Param("userUuid") String userUuid, 
			@Param("offset") Integer offset, @Param("limit") Integer limit);
	Integer insertPurchaseRecord(PurchaseRecord purchaseRecord);
	Integer countByUserUuid(@Param("userUuid") String userUuid);

}
