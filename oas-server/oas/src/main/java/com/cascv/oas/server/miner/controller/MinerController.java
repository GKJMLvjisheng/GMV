package com.cascv.oas.server.miner.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.wrapper.MinerDelete;
import com.cascv.oas.server.miner.wrapper.MinerRequest;
import com.cascv.oas.server.miner.wrapper.MinerUpdate;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/miner")
@Slf4j
public class MinerController {
	
	@Autowired
	private MinerMapper minerMapper;
	
	@PostMapping(value = "/inquireMiner")  
	@ResponseBody
	public ResponseEntity<?> inquireMiner(){
		Map<String,Object> info=new HashMap<>();
		List<MinerModel> minerModelList = minerMapper.selectAllMiner();
		if(minerModelList.size() > 0)
			info.put("minerModelList", minerModelList);
		else
			log.info("no message");
		return new ResponseEntity.Builder<List<MinerModel>>()
				.setData(minerModelList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/addMiner")  
	@ResponseBody
	public ResponseEntity<?> addMiner(@RequestBody MinerRequest minerRequest){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		MinerModel minerModel = new MinerModel();
		minerModel.setMinerCode(UuidUtils.getPrefixUUID(UuidPrefix.MINER_MODEL));
		log.info(minerModel.getMinerCode());
		minerModel.setMinerName(minerRequest.getMinerName());
		minerModel.setMinerDescription(minerRequest.getMinerDescription());
		minerModel.setMinerPeriod(minerRequest.getMinerPeriod());
		minerModel.setMinerPrice(minerRequest.getMinerPrice());
		minerModel.setMinerEfficiency(minerRequest.getMinerEfficiency());
		minerModel.setCreated(now);
		minerModel.setUpdated(now);
		minerMapper.insertMiner(minerModel);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/deleteMiner")  
	@ResponseBody
	public ResponseEntity<?> deleteMiner(@RequestBody MinerDelete minerDelete){
		String minerCode = minerDelete.getMinerCode();
		log.info(minerCode);
		minerMapper.deleteMiner(minerCode);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	@PostMapping(value = "/updateMiner")  
	@ResponseBody
	public ResponseEntity<?> updateMiner(@RequestBody MinerUpdate minerUpdate){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		MinerModel minerModel = new MinerModel();
		minerModel.setMinerCode(minerUpdate.getMinerCode());
		minerModel.setMinerName(minerUpdate.getMinerName());
		minerModel.setMinerDescription(minerUpdate.getMinerDescription());
		minerModel.setMinerEfficiency(minerUpdate.getMinerEfficiency());
		minerModel.setMinerPrice(minerUpdate.getMinerPrice());
		minerModel.setMinerPeriod(minerUpdate.getMinerPeriod());
		minerModel.setUpdated(now);
		minerMapper.updateMiner(minerModel);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}

}
