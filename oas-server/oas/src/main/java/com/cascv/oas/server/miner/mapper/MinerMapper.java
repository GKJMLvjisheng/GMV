package com.cascv.oas.server.miner.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.wrapper.UserMinerWrapper;

@Component
public interface MinerMapper {
	
	Integer insertMiner(MinerModel minerModel);
	Integer deleteMiner(@Param("minerCode") String minerCode);
	Integer updateMiner(MinerModel minerModel);
	List<MinerModel> selectAllMiner();
	MinerModel inquireByUuid(@Param("minerCode") String minerCode);
	MinerModel inquireByMinerName(@Param("minerName") String minerName);
	MinerModel inquireUpdateMinerName(@Param("minerName") String minerName, @Param("updateMinerName") String updateMinerName);
	
	List<UserMinerWrapper> selectByuserUuid(@Param("userUuid") String userUuid);

}
