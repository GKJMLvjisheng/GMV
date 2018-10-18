package com.cascv.oas.server.miner.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.PageDomain;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.service.MinerService;
import com.cascv.oas.server.miner.wrapper.InquireRequest;
import com.cascv.oas.server.miner.wrapper.MinerDelete;
import com.cascv.oas.server.miner.wrapper.MinerRequest;
import com.cascv.oas.server.miner.wrapper.MinerUpdate;
import com.cascv.oas.server.miner.wrapper.PurchaseRecordWrapper;
import com.cascv.oas.server.miner.wrapper.UserBuyMinerRequest;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/v1/miner")
@Slf4j
public class MinerController {
	
	@Autowired
	private MinerMapper minerMapper;
	
	@Autowired
	private MinerService minerService;
	
	@Autowired
	private TimeZoneService timeZoneService;
	
	@Autowired
	private UserWalletMapper userWalletMapper;
	
	@Autowired
	private UserWalletService userWalletService;
	
	@Autowired
	private ActivityMapper activityMapper;


	//新增矿机时查询矿机名是否重复
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
	
	//修改矿机信息时查询矿机名是否重复
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
	
	//给web端矿机详情
	@PostMapping(value = "/inquireWebMiner")  
	@ResponseBody
	public ResponseEntity<?> inquireWebMiner(){
		List<MinerModel> minerModelList = minerMapper.selectAllWebMiner();
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
	
	
	//安卓端的矿机详情，分页
	@PostMapping(value = "/inquireMiner")  
	@ResponseBody
	public ResponseEntity<?> inquireMiner(@RequestBody PageDomain<Integer> pageInfo){
		Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer limit = pageSize;
        Integer offset;
 
        if (limit == null) {
          limit = 10;
        }
        
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
        
		List<MinerModel> minerModelList = minerService.selectAllMiner(offset, limit);
		
		Integer count = minerMapper.countNum();
		PageDomain<MinerModel> minerModelDetail = new PageDomain<>();
		minerModelDetail.setTotal(count);
		minerModelDetail.setAsc("desc");
		minerModelDetail.setOffset(offset);
		minerModelDetail.setPageNum(pageNum);
		minerModelDetail.setPageSize(pageSize);
		minerModelDetail.setRows(minerModelList);
		return new ResponseEntity.Builder<PageDomain<MinerModel>>()
				.setData(minerModelDetail)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	//新增矿机
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
	
	//删除矿机
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
	
	//更新矿机
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
	
	//购买矿机
	@PostMapping(value = "/buyMiner")  
	@ResponseBody
	public ResponseEntity<?> buyMiner(@RequestBody UserBuyMinerRequest userBuyMinerRequest){
		String userUuid = ShiroUtils.getUserUuid();
		String updated = DateUtils.dateTimeNow(DateUtils.YYYYMMDDHHMMSS);
		UserWallet userWallet = userWalletMapper.selectByUserUuid(userUuid);
		String minerName = userBuyMinerRequest.getMinerName();
		Integer minerNum = userBuyMinerRequest.getMinerNum();
		BigDecimal priceSum = userBuyMinerRequest.getPriceSum();
		BigDecimal balance = userWalletMapper.selectByUserUuid(userUuid).getBalance();
		BigDecimal minerPower = minerMapper.inquireByMinerName(minerName).getMinerPower();
		BigDecimal powerSum = minerPower.multiply(BigDecimal.valueOf((int)minerNum));
		//判断自己剩余的OAS代币是否支持购买所需的矿机
		if(balance.compareTo(priceSum) != -1) {
			//增加算力球
			minerService.addMinerPowerBall(userUuid, powerSum);
			//增加算力提升记录(有效期)
			minerService.addMinerPowerTradeRecord(userUuid, powerSum);
			//增加一条购买记录
			minerService.addPurchaseRecord(userUuid, minerName, minerNum, priceSum);
			//增加在线钱包的消费记录
			userWalletService.addDetail(userWallet, "", UserWalletDetailScope.PURCHASE_MINER, priceSum, priceSum.toString(), "");
			//更新用户钱包
			log.info("walletUuid={}", userWalletMapper.selectByUserUuid(userUuid).getUuid());
			userWalletMapper.decreaseBalance(userWalletMapper.selectByUserUuid(userUuid).getUuid(), priceSum);
			//更新用户能量钱包，即提升算力
			activityMapper.increasePower(userUuid, powerSum, updated);
			return new ResponseEntity.Builder<Integer>()
					.setData(0)
					.setErrorCode(ErrorCode.SUCCESS)
					.build();
		}else {
			return new ResponseEntity.Builder<Integer>()
					.setData(0)
					.setErrorCode(ErrorCode.BALANCE_NOT_ENOUGH)
					.build();
		}
		
	}
	
	//查询用户矿机购买记录，分页
	@PostMapping(value = "/inquirePurchaseRecord")  
	@ResponseBody
	public ResponseEntity<?> inquirePurchaseRecord(@RequestBody PageDomain<Integer> pageInfo){
		String userUuid = ShiroUtils.getUserUuid();
		Integer pageNum = pageInfo.getPageNum();
        Integer pageSize = pageInfo.getPageSize();
        Integer limit = pageSize;
        Integer offset;
 
        if (limit == null) {
          limit = 10;
        }
        
        if (pageNum != null && pageNum > 0)
        	offset = (pageNum - 1) * limit;
        else 
        	offset = 0;
        
        List<PurchaseRecordWrapper> purchaseRecordList = minerService.inquerePurchaseRecord(userUuid, offset, limit);
        
        Integer count = minerMapper.countByUserUuid(userUuid);
        PageDomain<PurchaseRecordWrapper> purchaseRecordDetail = new PageDomain<>();
        purchaseRecordDetail.setAsc("desc");
        purchaseRecordDetail.setOffset(offset);
        purchaseRecordDetail.setPageNum(pageNum);
        purchaseRecordDetail.setPageSize(pageSize);
        purchaseRecordDetail.setRows(purchaseRecordList);
        purchaseRecordDetail.setTotal(count);
		return new ResponseEntity.Builder<PageDomain<PurchaseRecordWrapper>>()
				.setData(purchaseRecordDetail)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}

}
