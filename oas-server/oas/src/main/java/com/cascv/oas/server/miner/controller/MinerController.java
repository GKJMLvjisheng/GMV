package com.cascv.oas.server.miner.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
import com.cascv.oas.server.log.annotation.WriteLog;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.model.MinerModel;
import com.cascv.oas.server.miner.model.SystemParameterModel;
import com.cascv.oas.server.miner.service.MinerService;
import com.cascv.oas.server.miner.wrapper.AccountMiner;
import com.cascv.oas.server.miner.wrapper.AccountTypeMiner;
import com.cascv.oas.server.miner.wrapper.AddSystemParameter;
import com.cascv.oas.server.miner.wrapper.InquireRequest;
import com.cascv.oas.server.miner.wrapper.MinerDelete;
import com.cascv.oas.server.miner.wrapper.MinerRequest;
import com.cascv.oas.server.miner.wrapper.MinerUpdate;
import com.cascv.oas.server.miner.wrapper.PurchaseRecordWrapper;
import com.cascv.oas.server.miner.wrapper.SystemParameterModelRequest;
import com.cascv.oas.server.miner.wrapper.SystemParameterResponse;
import com.cascv.oas.server.miner.wrapper.UserBuyMinerRequest;
import com.cascv.oas.server.miner.wrapper.UserMinerInfo;
import com.cascv.oas.server.miner.wrapper.UserMinerUpdate;
import com.cascv.oas.server.miner.wrapper.UserPurchaseRecord;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.user.mapper.UserModelMapper;
import com.cascv.oas.server.user.model.UserModel;
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
	@Autowired
	private UserModelMapper userModelMapper;

	//增加系统变量
	@PostMapping(value = "/addSystemParameter")  
	@ResponseBody
	@WriteLog(value="addSystemParameter")
	public ResponseEntity<?> addSystemParameter(@RequestBody AddSystemParameter systemParameterModelRequest){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		SystemParameterModel systemParameterModel = new SystemParameterModel();
		BigDecimal parameterValue = systemParameterModelRequest.getParameterValue();
		BigDecimal rate = BigDecimal.ONE.divide(parameterValue, 8, BigDecimal.ROUND_HALF_UP);
		log.info("rate={}",rate);
		systemParameterModel.setParameterValue(rate);
		systemParameterModel.setPeriod(systemParameterModelRequest.getPeriod());
		systemParameterModel.setComment("inherit");
		systemParameterModel.setCurrency(1);
		systemParameterModel.setCreated(now);
		systemParameterModel.setUpdated(now);
		minerMapper.insertSystemParameter(systemParameterModel);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();		
	}
	
//	//
//	@PostMapping(value = "/deleteSystemParameter")  
//	@ResponseBody
//	public ResponseEntity<?> deleteSystemParameter(@RequestBody DeleteSystemParameter deleteSystemParameter){
//		log.info("uuid={}", deleteSystemParameter.getUuid());
//		minerMapper.deleteSystemParameterByUuid(deleteSystemParameter.getUuid());
//		return new ResponseEntity.Builder<Integer>()
//				.setData(0)
//				.setErrorCode(ErrorCode.SUCCESS)
//				.build();
//		
//	}
	
	//修改系统变量值
	@PostMapping(value = "/updatedSystemParameter")  
	@ResponseBody
	@WriteLog(value="updatedSystemParameter")
	public ResponseEntity<?> updatedSystemParameter(@RequestBody SystemParameterModelRequest systemParameterModelRequest){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		SystemParameterModel systemParameterModel = new SystemParameterModel();
		systemParameterModel.setCurrency(systemParameterModelRequest.getCurrency());
		systemParameterModel.setPeriod(systemParameterModelRequest.getTime());
		BigDecimal parameterValue = systemParameterModelRequest.getParameterValue();
		BigDecimal rate = BigDecimal.ONE.divide(parameterValue, 8, BigDecimal.ROUND_HALF_UP);
		log.info("rate={}",rate);
		systemParameterModel.setParameterValue(rate);
		systemParameterModel.setUpdated(now);
        minerMapper.updateSystemParameterByUuid(systemParameterModel);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	//显示系统变量
	@PostMapping(value = "/selectSystemParameter")  
	@ResponseBody
	public ResponseEntity<?> selectSystemParameter(){
		List<SystemParameterModel> systemParameterModel = minerMapper.selectSystemParameter();
		List<SystemParameterResponse> systemParameterResponseList = new ArrayList<>();
		for(int i=0; i<systemParameterModel.size(); i++) {
			SystemParameterResponse systemParameterResponse = new SystemParameterResponse();
			systemParameterResponse.setComment(systemParameterModel.get(i).getComment());
			String srcFormater="yyyy-MM-dd HH:mm:ss";
		    String dstFormater="yyyy-MM-dd HH:mm:ss";
			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
			String created=DateUtils.string2Timezone(srcFormater, systemParameterModel.get(i).getCreated(), dstFormater, dstTimeZoneId);
			String updated=DateUtils.string2Timezone(srcFormater, systemParameterModel.get(i).getUpdated(), dstFormater, dstTimeZoneId);
			systemParameterResponse.setCreated(created);
			systemParameterResponse.setUpdated(updated);
			systemParameterResponse.setCurrency(systemParameterModel.get(i).getCurrency());			
			systemParameterResponse.setPeriod(systemParameterModel.get(i).getPeriod());
			if(systemParameterModel.get(i).getCurrency() == 11) {
				BigDecimal parameter = systemParameterModel.get(i).getParameterValue();
				BigDecimal parameterValue = BigDecimal.ONE.divide(parameter, 2, BigDecimal.ROUND_HALF_UP);
				systemParameterResponse.setParameterValue(parameterValue);
				systemParameterResponse.setParameterName("β");
			}
			else {
				systemParameterResponse.setParameterName("γ");
				BigDecimal rate = systemParameterModel.get(i).getParameterValue();
				log.info("rate={}",rate);
				BigDecimal parameterValue = BigDecimal.ONE.divide(rate, 2, BigDecimal.ROUND_HALF_UP);
				log.info("parameterValue={}",parameterValue);
				systemParameterResponse.setParameterValue(parameterValue);
			}
			systemParameterResponseList.add(systemParameterResponse);
		}
		return new ResponseEntity.Builder<List<SystemParameterResponse>>()
				.setData(systemParameterResponseList)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	
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
		minerModelDetail.setAsc("asc");
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
	@WriteLog(value="AddMiner")
	public ResponseEntity<?> addMiner(@RequestBody MinerRequest minerRequest){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		Integer count = minerMapper.selectAllWebMiner().size();
		MinerModel minerModel = new MinerModel();
		minerModel.setMinerCode(UuidUtils.getPrefixUUID(UuidPrefix.MINER_MODEL));
		log.info(minerModel.getMinerCode());
		minerModel.setMinerName(minerRequest.getMinerName());
		minerModel.setMinerDescription(minerRequest.getMinerDescription());
		minerModel.setMinerPeriod(minerRequest.getMinerPeriod());
		minerModel.setMinerPrice(minerRequest.getMinerPrice());
		minerModel.setMinerGrade(minerRequest.getMinerGrade());
		minerModel.setMinerPower(minerRequest.getMinerPower());
		minerModel.setOrderNum(count+1);
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
	@WriteLog(value="DeleteMiner")
	public ResponseEntity<?> deleteMiner(@RequestBody MinerDelete minerDelete){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		String minerCode = minerDelete.getMinerCode();
		Integer orderNum = minerMapper.inquireByUuid(minerCode).getOrderNum();
		Integer count = minerMapper.selectAllWebMiner().size();
		log.info("orderNum={}",orderNum);
		log.info("minerMapper.selectAllWebMiner().size()={}",minerMapper.selectAllWebMiner().size());
		minerMapper.deleteMiner(minerCode);
		for(;orderNum < count; orderNum++) {
			Integer newOrderNum = orderNum + 1;
			String newMinerCode = minerMapper.inquireByOrderNum(newOrderNum).getMinerCode();
			minerMapper.updateOrderNum(newMinerCode, orderNum, now);
		}
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
	}
	
	//更新矿机
	@PostMapping(value = "/updateMiner")  
	@ResponseBody
	@WriteLog(value="updatedMiner")
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
//	
//	//目前矿机总数
//	@PostMapping(value = "/countMiner")  
//	@ResponseBody
//	public ResponseEntity<?> countMiner(){
//		Integer count = minerMapper.selectAllWebMiner().size();
//		return new ResponseEntity.Builder<Integer>()
//				.setData(count)
//				.setErrorCode(ErrorCode.SUCCESS)
//				.build();
//	}
	
	//购买矿机
	@PostMapping(value = "/buyMiner")  
	@ResponseBody
	@Transactional
	@WriteLog(value="buyMiner")
	public ResponseEntity<?> buyMiner(@RequestBody UserBuyMinerRequest userBuyMinerRequest){
		String userUuid = ShiroUtils.getUserUuid();
		log.info("userUuid={}", userUuid);
		String updated = DateUtils.dateTimeNow(DateUtils.YYYYMMDDHHMMSS);
		UserWallet userWallet = userWalletMapper.selectByUserUuid(userUuid);
		String minerName = userBuyMinerRequest.getMinerName();
		Integer minerNum = userBuyMinerRequest.getMinerNum();
		BigDecimal priceSum = userBuyMinerRequest.getPriceSum();
		BigDecimal balance = userWalletMapper.selectByUserUuid(userUuid).getBalance();
		BigDecimal minerPower = minerMapper.inquireByMinerName(minerName).getMinerPower();
		BigDecimal powerSum = minerPower.multiply(BigDecimal.valueOf((int)minerNum));
		//查询购买是否为三级矿机
		Integer minerGrade=minerMapper.inquireByMinerName(minerName).getMinerGrade();
		UserModel userModel=userModelMapper.selectByUuid(userUuid);
		Integer minerThreeRestriction=userModel.getMinerThreeRestriction();
		if(minerGrade != 3) {
		//判断自己剩余的OAS代币是否支持购买所需的矿机
		if(balance.compareTo(priceSum) != -1) {
			//更新用户钱包
			log.info("walletUuid={}", userWalletMapper.selectByUserUuid(userUuid).getUuid());
			UserWallet tuserWallet = userWalletMapper.selectByUserUuid(userUuid);
			userWalletMapper.decreaseBalance(tuserWallet.getUuid(), priceSum);
			//增加在线钱包的消费记录
			log.info("commet={}", minerNum+"台"+minerName);
			userWalletService.addDetail(userWallet, "", UserWalletDetailScope.PURCHASE_MINER, priceSum, minerNum+"台"+minerName, "",tuserWallet.getBalance().subtract(priceSum));
			//增加算力球
			minerService.addMinerPowerBall(userUuid, powerSum);
			//增加算力提升记录(有效期)
			minerService.addMinerPowerTradeRecord(userUuid, powerSum);
			//增加一条购买记录
			minerService.addPurchaseRecord(userUuid, minerName, minerNum, priceSum);
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
		
	}else {
		if(minerThreeRestriction == 1) {
			if(balance.compareTo(priceSum) != -1) {
				//更新用户钱包
				log.info("walletUuid={}", userWalletMapper.selectByUserUuid(userUuid).getUuid());
				UserWallet tuserWallet = userWalletMapper.selectByUserUuid(userUuid);
				userWalletMapper.decreaseBalance(tuserWallet.getUuid(), priceSum);
				//增加在线钱包的消费记录
				log.info("commet={}", minerNum+"台"+minerName);
				userWalletService.addDetail(userWallet, "", UserWalletDetailScope.PURCHASE_MINER, priceSum, minerNum+"台"+minerName, "",tuserWallet.getBalance().subtract(priceSum));
				//增加算力球
				minerService.addMinerPowerBall(userUuid, powerSum);
				//增加算力提升记录(有效期)
				minerService.addMinerPowerTradeRecord(userUuid, powerSum);
				//增加一条购买记录
				minerService.addPurchaseRecord(userUuid, minerName, minerNum, priceSum);
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
		}else {
			return new ResponseEntity.Builder<Integer>()
					.setData(0)
					.setErrorCode(ErrorCode.BUYGRADETHREE_MINER_NO_AUTHORIZATION)
					.build();
		}
		 }
		
	}
	
	//上移(自己的orderNum减1，换位置的对方加1)
	@PostMapping(value = "/upMiner")  
	@ResponseBody
	@WriteLog(value="upMiner")
	public ResponseEntity<?> upMiner(@RequestBody MinerDelete minerDelete){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		String minerCode = minerDelete.getMinerCode();
		Integer orderNum = minerMapper.inquireByUuid(minerCode).getOrderNum();
		if(orderNum == 1) {
			return new ResponseEntity.Builder<Integer>()
					.setData(0)
					.setErrorCode(ErrorCode.THE_FIRST_ONE)
					.build();
		}
		Integer newOrderNum = orderNum - 1;
		log.info("newOrderNum={}",newOrderNum);
		String newMinerCode = minerMapper.inquireByOrderNum(newOrderNum).getMinerCode();
		orderNum = orderNum - 1;
		newOrderNum = newOrderNum + 1;
		log.info("orderNum={}",orderNum);
		log.info("newOrderNum={}",newOrderNum);
		minerMapper.updateOrderNum(minerCode, orderNum, now);
		minerMapper.updateOrderNum(newMinerCode, newOrderNum, now);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	//下移(自己的orderNum加1，换位置的对方减1)
	@PostMapping(value = "/downMiner")  
	@ResponseBody
	@WriteLog(value="downMiner")
	public ResponseEntity<?> downMiner(@RequestBody MinerDelete minerDelete){
		String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
		String minerCode = minerDelete.getMinerCode();
		Integer orderNum = minerMapper.inquireByUuid(minerCode).getOrderNum();
		if(orderNum == minerMapper.selectAllWebMiner().size()) {
			return new ResponseEntity.Builder<Integer>()
					.setData(0)
					.setErrorCode(ErrorCode.THE_LAST_ONE)
					.build();
		}
		Integer newOrderNum = orderNum + 1;
		log.info("newOrderNum={}",newOrderNum);
		String newMinerCode = minerMapper.inquireByOrderNum(newOrderNum).getMinerCode();
		orderNum = orderNum + 1;
		newOrderNum = newOrderNum - 1;
		log.info("orderNum={}",orderNum);
		log.info("newOrderNum={}",newOrderNum);
		minerMapper.updateOrderNum(minerCode, orderNum, now);
		minerMapper.updateOrderNum(newMinerCode, newOrderNum, now);
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
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
	
	//web端查询用户矿机购买详情记录，分页
	@PostMapping(value = "/inquireUserPurchaseRecord")  
	@ResponseBody
	public ResponseEntity<?> inquireUserPurchaseRecord(@RequestBody PageDomain<Integer> pageInfo){
		Integer pageNum = pageInfo.getPageNum();
	    Integer pageSize = pageInfo.getPageSize();
	    Integer limit = pageSize;
	    Integer offset;

	    if (pageSize == 0) {
	      limit = 10;
	    }
	    if (pageNum != null && pageNum > 0)
	    	offset = (pageNum - 1) * limit;
	    else 
	    	offset = 0;
	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	    List<UserPurchaseRecord> userPurchaseRecord = minerMapper.selectUserPurchaseRecord(searchValue, offset, limit);
	    for(UserPurchaseRecord purchaseRecord : userPurchaseRecord) {
	    	String srcFormater="yyyy-MM-dd HH:mm:ss";
  			String dstFormater="yyyy-MM-dd HH:mm:ss";
  			String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
  			String created=DateUtils.string2Timezone(srcFormater, purchaseRecord.getCreated(), dstFormater, dstTimeZoneId);
  			purchaseRecord.setCreated(created);
	    }
	    PageDomain<UserPurchaseRecord> purchaseRecordPage = new PageDomain<>();
	    Integer count = minerMapper.countBySearchValue(searchValue);
	    purchaseRecordPage.setTotal(count);
	    purchaseRecordPage.setAsc("desc");
	    purchaseRecordPage.setOffset(offset);
	    purchaseRecordPage.setPageNum(pageNum);
	    purchaseRecordPage.setPageSize(pageSize);
	    purchaseRecordPage.setRows(userPurchaseRecord);
		return new ResponseEntity.Builder<PageDomain<UserPurchaseRecord>>()
				.setData(purchaseRecordPage)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	//查询系统一共卖出了多少台矿机
	@PostMapping(value = "/inquireSumMinerNum")  
	@ResponseBody
	public ResponseEntity<?> inquireSumMinerNum(){
		//系统卖出的总矿机数
		Integer minerNumSum = minerMapper.inquireSumMinerNum();
		//系统卖出的不同类型的矿机的总数
		List<AccountTypeMiner> accountTypeMiner = minerMapper.inquireTypeMinerNum();
		AccountMiner accountMiner = new AccountMiner();
		if(minerNumSum == null) {
			minerNumSum = 0;
		}
		accountMiner.setMinerNumSum(minerNumSum);
		accountMiner.setAccountTypeMiner(accountTypeMiner);
		return new ResponseEntity.Builder<AccountMiner>()
				.setData(accountMiner)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	/**
	 * @category 查看用户和矿机限购信息
	 * @author lvjisheng
	 * @param searchValue, offset, limit
	 * @return 
	 */
	@PostMapping(value = "/inquireMinerOfUser")  
	@ResponseBody
	public ResponseEntity<?> inquireMinerOfUser(@RequestBody PageDomain<Integer> pageInfo){
		Integer pageNum = pageInfo.getPageNum();
	    Integer pageSize = pageInfo.getPageSize();
	    Integer limit = pageSize;
	    Integer offset;

	    if (pageSize ==null) {
	      limit = 10;
	    }
	    if (pageNum != null && pageNum > 0)
	    	offset = (pageNum - 1) * limit;
	    else 
	    	offset = 0;
	    String searchValue=pageInfo.getSearchValue();//后端搜索关键词支持
	    	    
	    List<UserPurchaseRecord> purchaseRecords =minerMapper.inquireMinerOfUser(searchValue, offset, limit);
	    
	    PageDomain<UserPurchaseRecord> purchaseRecordPage = new PageDomain<>();
	    Integer count = minerMapper.countUsersBySearchValue(searchValue);
	    purchaseRecordPage.setTotal(count);
	    purchaseRecordPage.setAsc("desc");
	    purchaseRecordPage.setOffset(offset);
	    purchaseRecordPage.setPageNum(pageNum);
	    purchaseRecordPage.setPageSize(pageSize);
	    purchaseRecordPage.setRows(purchaseRecords);
				
		return new ResponseEntity.Builder<PageDomain<UserPurchaseRecord>>()
				.setData(purchaseRecordPage)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
	}
	
	/**
	 * @category 修改用户矿机限购信息
	 * @author lvjisheng
	 * @param startTime,endTime,restriction,uuid
	 * @return 
	 */
	@PostMapping(value = "/updateUserMinerInfo")  
	@ResponseBody
	@WriteLog(value="updateUserMinerInfo")
	public ResponseEntity<?> updateUserMinerInfo(@RequestBody UserMinerUpdate userMinerUpdate){
        
		List<UserMinerInfo> umInfos=userMinerUpdate.getUmInfos();
		for(UserMinerInfo umInfo:umInfos){
			if(umInfo.getUuid()!=null)
			minerMapper.updateUserMinerInfo(umInfo.getUuid(), umInfo.getStartTime(), umInfo.getEndTime(), umInfo.getRestriction());
		}		
		return new ResponseEntity.Builder<Integer>()
				.setData(0)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();		
	}	
}
