package com.cascv.oas.server.miner.controller;

import java.util.List;

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
import com.cascv.oas.server.miner.wrapper.InquireRequest;
import com.cascv.oas.server.miner.wrapper.MinerDelete;
import com.cascv.oas.server.miner.wrapper.MinerRequest;
import com.cascv.oas.server.miner.wrapper.MinerUpdate;
import com.cascv.oas.server.timezone.service.TimeZoneService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/miner")
@Slf4j
public class MinerController {
	
	@Autowired
	private MinerMapper minerMapper;
	
	@Autowired
	private TimeZoneService timeZoneService;

	
	@PostMapping(value = "/inquireMinerName")  
	@ResponseBody
	public ResponseEntity<?> inquireMinerName(@RequestBody InquireRequest inquireRequest){
		String minerName = inquireRequest.getMinerName();
		if(minerMapper.inquireByMinerName(minerName) == null) {
			return new ResponseEntity.Builder<Integer>()
			        .setData(0)
			        .setErrorCode(ErrorCode.SUCCESS).build();
		}else {
			return new ResponseEntity.Builder<Integer>()
			        .setData(0)
			        .setErrorCode(ErrorCode.GENERAL_ERROR).build();
		}
		
	}
	
	@PostMapping(value = "/inquireUpdateMinerName")  
	@ResponseBody
	public ResponseEntity<?> inquireUpdateMinerName(@RequestBody InquireRequest inquireRequest){
		String minerCode = inquireRequest.getMinerCode();
		String minerName = minerMapper.inquireByUuid(minerCode).getMinerName();
		String updateMinerName = inquireRequest.getMinerName();
		if(minerMapper.inquireUpdateMinerName(minerName, updateMinerName) == null) {
			return new ResponseEntity.Builder<Integer>()
			        .setData(0)
			        .setErrorCode(ErrorCode.SUCCESS).build();
		}else {
			return new ResponseEntity.Builder<Integer>()
			        .setData(0)
			        .setErrorCode(ErrorCode.GENERAL_ERROR).build();
		}
	}
	
	@PostMapping(value = "/inquireMiner")  
	@ResponseBody
	public ResponseEntity<?> inquireMiner(){
		List<MinerModel> minerModelList = minerMapper.selectAllMiner();
		for(MinerModel minerModel : minerModelList) {
			String srcFormater="yyyy-MM-dd HH:mm:ss";
		    String dstFormater="yyyy-MM-dd HH:mm:ss";
		    String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
		    String updated=DateUtils.string2Timezone(srcFormater, minerModel.getUpdated(), dstFormater, dstTimeZoneId);
		    minerModel.setUpdated(updated);
			log.info("updated={}", minerModel.getUpdated());
		}
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
		minerModel.setMinerGrade(minerRequest.getMinerGrade());
		minerModel.setMinerPower(minerRequest.getMinerPower());
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
		minerModel.setMinerPower(minerUpdate.getMinerPower());
		minerModel.setMinerPrice(minerUpdate.getMinerPrice());
		minerModel.setMinerGrade(minerUpdate.getMinerGrade());
		minerModel.setMinerPeriod(minerUpdate.getMinerPeriod());
		minerModel.setUpdated(now);
		log.info("updated={}", minerModel.getUpdated());
		minerMapper.updateMiner(minerModel);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}

}
