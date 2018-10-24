package com.cascv.oas.server.reward.service;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.core.utils.UuidUtils;
import com.cascv.oas.server.activity.mapper.ActivityMapper;
import com.cascv.oas.server.activity.model.EnergyPowerBall;
import com.cascv.oas.server.activity.model.PowerTradeRecord;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.common.UuidPrefix;
import com.cascv.oas.server.energy.mapper.EnergyBallMapper;
import com.cascv.oas.server.exchange.constant.CurrencyCode;
import com.cascv.oas.server.exchange.model.ExchangeRateModel;
import com.cascv.oas.server.exchange.service.ExchangeRateService;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.model.PurchaseRecord;
import com.cascv.oas.server.reward.job.DelayRewardJob;
import com.cascv.oas.server.reward.job.ImmediatelyRewardJob;
import com.cascv.oas.server.reward.mapper.PromotedRewardModelMapper;
import com.cascv.oas.server.reward.model.PromotedRewardModel;
import com.cascv.oas.server.scheduler.service.SchedulerService;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.user.mapper.UserModelMapper;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.service.PermService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PromotedRewardService {
	@Autowired 
	private PromotedRewardModelMapper promotedRewardModelMapper;
	@Autowired 
	private TimeZoneService timeZoneService;
	@Autowired 
	private UserModelMapper userModelMapper;
	@Autowired 
	private UserWalletMapper userWalletMapper;
	@Autowired 
	private MinerMapper minerMapper;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private ExchangeRateService exchangeRateService;
	@Autowired
	private PermService permService;
	@Autowired 
	private UserWalletDetailMapper userWalletDetailMapper;
	@Autowired 
	private EnergyBallMapper energyBallMapper;
	@Autowired
	private ActivityMapper activityMapper;

	private static final Integer STATUS_ACTIVITY_OF_MINER = 1;  //矿机处于工作状态
	private static final Integer STATUS_DIE_OF_MINER = 0;  //矿机处于工作状态
	private static final Integer ENEGY_IN = 1;               // 能量增加为1，能量减少为0
	private static final Integer ENEGY_OUT = 0;               // 能量增加为1，能量减少为0
	private static final Integer REWARD_CODE_OF_MINER = 11;  //矿机推广奖励
	private static final Integer REWARD_CODE_OF_DIE_MINER = 12;  //矿机推广奖励到期
	
	private EnergyPowerBall rewardEnergyPowerBall = new EnergyPowerBall();
	private EnergyPowerBall decreaseRewardEnergyPowerBall = new EnergyPowerBall();
	
	public List<PromotedRewardModel> selectAllPromotedRewardConfig(){
		List<PromotedRewardModel> promotedRewardModelList = promotedRewardModelMapper.selectAllPromotedRewards();

			for(PromotedRewardModel promotedRewardModel : promotedRewardModelList)
			{
				String srcFormater="yyyy-MM-dd HH:mm:ss";
				String dstFormater="yyyy-MM-dd HH:mm:ss";
				String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
				String created=DateUtils.string2Timezone(srcFormater, promotedRewardModel.getCreated(), dstFormater, dstTimeZoneId);
				promotedRewardModel.setCreated(created);
				log.info("newCreated={}",created);
			}
		
		return promotedRewardModelList;
	}
	/**
	 * @author Ming Yang
	 * 
	 *     开启查询用户是否购买矿机job
	 */
	  @PostConstruct
	  public void startJob() {
	    JobDetail immediatelyJobDetail = JobBuilder.newJob(ImmediatelyRewardJob.class)
	        .withIdentity("JobDetailC", "groupC").build();
	    JobDetail delayJobDetail = JobBuilder.newJob(DelayRewardJob.class)
		        .withIdentity("JobDetailD", "groupD").build();
	    Trigger immediatelyTrigger = TriggerBuilder.newTrigger().withIdentity("triggerC", "groupC")
	        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
	            .withIntervalInSeconds(30).repeatForever()).startNow().build();
	    Trigger delayTrigger = TriggerBuilder.newTrigger().withIdentity("triggerD", "groupD")
		        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
		            .withIntervalInSeconds(60).repeatForever()).startNow().build();
	    immediatelyJobDetail.getJobDataMap().put("promotedRewardService", this);
	    delayJobDetail.getJobDataMap().put("promotedRewardService", this);
	    schedulerService.addJob(immediatelyJobDetail, immediatelyTrigger);
	    schedulerService.addJob(delayJobDetail, delayTrigger);
	    log.info("add reward job ...");
	  }
	  /**
	   * @author Ming Yang
	   *           定是查询是否有用户购买矿机
	   */
	  public synchronized void checkUserWhetherBuyMiner() {
		  if(permService.getPromotionPerm() == true) {
		  log.info(" check all users whether buy miner give them immediately oas reward ...");
		  List<PurchaseRecord> purchaseRecordList=minerMapper.selectByMinerPurchaseStatus();
		  if (purchaseRecordList != null && purchaseRecordList.size() > 0) {
			  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
				  String userUuid=purchaseRecord.getUserUuid();
				  this.giveSuperiorsUserImmediatelyReward(purchaseRecord, userUuid);
				  minerMapper.updateByMinerPurchaseStatus(purchaseRecord);
				  log.info("end reward oas job ...");
			  }
		  }
		  }else {
			  log.info("无奖励");
			  List<PurchaseRecord> purchaseRecordList=minerMapper.selectByMinerPurchaseStatus();
			  if (purchaseRecordList != null && purchaseRecordList.size() > 0) {
				  for(PurchaseRecord purchaseRecord:purchaseRecordList) { 
					  minerMapper.updateByMinerPurchaseStatus(purchaseRecord);
				  }
			  }
		  }
	  }
	  
	  public synchronized void giveUserPowerRewardBuyMiner() {
		  if(permService.getPromotionPerm() == true) {
		  log.info(" check give user power reward...");
		  List<PurchaseRecord> purchaseRecordList =minerMapper.selectByMinerStatusPowerRewardStatus();
		  if (purchaseRecordList != null && purchaseRecordList.size() > 0) {
			  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
				  String userUuid=purchaseRecord.getUserUuid();
				  this.giveSuperiorsUserPowerReward(purchaseRecord, userUuid);
				  minerMapper.updateByPowerRewardStatus(purchaseRecord);
				  log.info("end reward power job ...");
			  }
		  }
		  }else {
			  log.info("无奖励");
			  List<PurchaseRecord> purchaseRecordList =minerMapper.selectByMinerStatusPowerRewardStatus();
			  if (purchaseRecordList != null && purchaseRecordList.size() > 0) {
				  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
					  minerMapper.updateByPowerRewardStatus(purchaseRecord);
				  }
			  }
		  }
	  }
	  public synchronized void decreaseUserPowerRewardBuyMiner() {
		  if(permService.getPromotionPerm() == true) {
		  log.info(" check decrease user power reward...");
		  List<PurchaseRecord> purchaseRecordList = minerMapper.selectByMinerStatusPowerRewardStatusToDecrease();
		  if (purchaseRecordList != null && purchaseRecordList.size() > 0) {
			  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
				  String userUuid=purchaseRecord.getUserUuid();
				  this.decreaseSuperiorsUserPowerReward(purchaseRecord, userUuid);
				  minerMapper.updateByPowerRewardStatusToDecrease(purchaseRecord);
				  log.info("end decrease power job ...");
			  }
		  }
		  }else {
			  log.info("无奖励");
			  List<PurchaseRecord> purchaseRecordList = minerMapper.selectByMinerStatusPowerRewardStatusToDecrease();
			  if (purchaseRecordList != null && purchaseRecordList.size() > 0) {
				  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
					  minerMapper.updateByPowerRewardStatusToDecrease(purchaseRecord);
				  }
			  }
		  }
	  }
	  
	  public synchronized void checkBuyUserMinerRedeem() {
		  if(permService.getPromotionPerm() == true) {
		  log.info("check all buy users whether buy miner redeem ...");
		  List<String> userUuidList=minerMapper.selectUserUuidByMinerStatus();//所有符合条件的用户
		  if(userUuidList!=null && userUuidList.size()>0) {
			  for(String userUuid:userUuidList) {
				  	this.doUserMinerRedeem(userUuid);
			  }
			  log.info("end reward buy miner redeem job ...");
		  }
		  }else {
			  log.info("无奖励");
			  List<String> userUuidList=minerMapper.selectUserUuidByMinerStatus();//所有符合条件的用户
			  if(userUuidList!=null && userUuidList.size()>0) {
				  for(String userUuid:userUuidList) {
					  List<PurchaseRecord> purchaseRecordList=minerMapper.selectByMinerStatus(userUuid);
					  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
						  Integer minerNum=purchaseRecord.getMinerNum();
						  purchaseRecord.setFinishRewardNumber(minerNum);
						  minerMapper.updateByMinerNumFinishRewardNumber(purchaseRecord);
					  }
				  }
			  }
		  }
	  	}
	  
	  /**
	   * @author Ming Yang
	   * @return
	   *             返回代币奖励冻结比例
	   */ 
	  public String getOasFrozenRewardRatio() {
		  String rewardCoinName="代币";
		  PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);
		  BigDecimal frozenRatio=promotedRewardModel.getFrozenRatio();
		  BigDecimal n=new BigDecimal(100);
		  frozenRatio=frozenRatio.multiply(n);
		  String frozenRatio2String=frozenRatio.toString();
		  frozenRatio2String=frozenRatio2String+"%";
		  return frozenRatio2String;
	  }
	  
	  /**
	   * @author Ming Yang
	   * @return
	   *             返回代币奖励比例
	   */ 
	  public String getOasRewardRatio() {
		  String rewardCoinName="代币";
		  PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);
		  BigDecimal frozenRatio=promotedRewardModel.getFrozenRatio();
		  BigDecimal a=new BigDecimal(1);
		  BigDecimal rewardRatio=a.subtract(frozenRatio);
		  BigDecimal n=new BigDecimal(100);
		  rewardRatio=rewardRatio.multiply(n);
		  String rewardRatio2String=rewardRatio.toString();
		  rewardRatio2String=rewardRatio2String+"%";
		  return rewardRatio2String;
	  }
	  
	  
	  /**
	   * @author Ming Yang
	   * @param userUuid
	   *         实现冻结代币返还逻辑
	   */
	  public void doUserMinerRedeem(String userUuid) {
		  List<PurchaseRecord> purchaseRecordList=minerMapper.selectByMinerStatus(userUuid);
		  BigDecimal minerPrice=purchaseRecordList.get(0).getMinerPrice();
		  Integer finishRewardNumber=purchaseRecordList.get(0).getFinishRewardNumber();
		  String startTime=purchaseRecordList.get(0).getFinishRewardUpdated();
		  String endTime=DateUtils.getTime();
		  BigDecimal userTimePointToOas=this.getUserTimePointToOas(userUuid,startTime,endTime);
		  BigDecimal remainTimeOas=purchaseRecordList.get(0).getRemainTimeOas();
		  remainTimeOas = remainTimeOas.add(userTimePointToOas);
		  if(remainTimeOas.compareTo(minerPrice)==1) {
			  finishRewardNumber=finishRewardNumber+1;
			  purchaseRecordList.get(0).setFinishRewardNumber(finishRewardNumber);
			  minerMapper.updateByFinishRewardNumber(purchaseRecordList.get(0));
			  remainTimeOas=remainTimeOas.subtract(minerPrice);//存储多余的代币，下次计算使用
			  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
				  purchaseRecord.setFinishRewardUpdated(endTime);
				  purchaseRecord.setRemainTimeOas(remainTimeOas);
				  minerMapper.updateByFinishRewardUpdated(purchaseRecord);
			  }
		  }else {
			  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
				  purchaseRecord.setFinishRewardUpdated(endTime);
				  purchaseRecord.setRemainTimeOas(remainTimeOas);
				  minerMapper.updateByFinishRewardUpdated(purchaseRecord);
			  }
		  }
	  }
	  
	  /**
	   * @author Ming Yang
	   * @param userName
	   * @return
	   *           获取加密字符串，保留后三位，其他用三个星代替
	   */
	  public String getEncryptionUserName(String userName) {
		  Integer length=userName.length();
		  if(length>3) {
		  Integer number=length-3;
		  String str="";
		  for(Integer i=0;i<number;i++) {
			  str=str+"*";
		  }
		  userName=userName.substring(length-3, length);
		  userName=str+userName;
		  }else if(length == 3){
		  userName=userName.substring(length-2, length);
		  userName="**"+userName;
		  }else if(length == 2) {
		  userName=userName.substring(length-1, length);
		  userName="*"+userName;
		  }else {
		  userName="*";
		  }	  
		  return userName;
	  }
	  
		/**
		 * @author Ming Yang
		 * Date:20181019
		 * @param userUuid
		 * @param powerSum
		 *            增加奖励算力球记录
		 */
		public void addRewardPowerBall(PurchaseRecord purchaseRecord,String userUuid, BigDecimal powerSum) {
			String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
			rewardEnergyPowerBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
			rewardEnergyPowerBall.setSourceCode(REWARD_CODE_OF_MINER);
			rewardEnergyPowerBall.setUserUuid(userUuid);
			rewardEnergyPowerBall.setStatus(STATUS_ACTIVITY_OF_MINER);
			rewardEnergyPowerBall.setPower(powerSum);
			rewardEnergyPowerBall.setCreated(now);
			rewardEnergyPowerBall.setUpdated(now);
			activityMapper.insertEnergyPowerBall(rewardEnergyPowerBall);
			//更新购买记录中算力球产生信息
			String rewardEnergyBallUuid=rewardEnergyPowerBall.getUuid();
			String purchaseRecordUuid=purchaseRecord.getUuid();
			purchaseRecord.setUuid(purchaseRecordUuid);
			purchaseRecord.setRewardEnergyBallUuid(rewardEnergyBallUuid);
			minerMapper.updateByRewardEnergyBallUuid(purchaseRecord);
		}
		
		/**
		 * @author Ming Yang
		 * @param purchaseRecord
		 * @param userUuid
		 * @param powerSum
		 *         增加删除算力的球
		 */
		public void addDecreaseRewardPowerBall(PurchaseRecord purchaseRecord,String userUuid, BigDecimal powerSum) {
			String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
			decreaseRewardEnergyPowerBall.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_POINT));
			decreaseRewardEnergyPowerBall.setSourceCode(REWARD_CODE_OF_DIE_MINER);
			decreaseRewardEnergyPowerBall.setUserUuid(userUuid);
			decreaseRewardEnergyPowerBall.setStatus(STATUS_DIE_OF_MINER);
			decreaseRewardEnergyPowerBall.setPower(powerSum);
			decreaseRewardEnergyPowerBall.setCreated(now);
			decreaseRewardEnergyPowerBall.setUpdated(now);
			activityMapper.insertEnergyPowerBall(decreaseRewardEnergyPowerBall);
			//更新购买记录中算力球产生信息
			String decreaseRewardEnergyBallUuid=decreaseRewardEnergyPowerBall.getUuid();
			String purchaseRecordUuid=purchaseRecord.getUuid();
			purchaseRecord.setUuid(purchaseRecordUuid);
			purchaseRecord.setRewardEnergyBallUuid(decreaseRewardEnergyBallUuid);
			minerMapper.updateByRewardEnergyBallUuid(purchaseRecord);
		}
		
		
		
		/**
		 * @author Ming Yang
		 * @param userUuid
		 * @param powerSum
		 *       增加算力奖励记录
		 */
		public void addRewardPowerTradeRecord(String userUuid, BigDecimal powerSum) {
			PowerTradeRecord powerTradeRecord = new PowerTradeRecord();
			String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
			powerTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
			powerTradeRecord.setUserUuid(userUuid);
			powerTradeRecord.setEnergyBallUuid(rewardEnergyPowerBall.getUuid());
			powerTradeRecord.setInOrOut(ENEGY_IN);
			powerTradeRecord.setPowerChange(powerSum);
			powerTradeRecord.setCreated(now);
			powerTradeRecord.setStatus(STATUS_ACTIVITY_OF_MINER);
			activityMapper.insertPowerTradeRecord(powerTradeRecord);
		}
		/**
		 * @author Ming Yang
		 * @param userUuid
		 * @param powerSum
		 *       增加算力消失原因记录
		 */
		public void addDecreaseRewardPowerTradeRecord(String userUuid, BigDecimal powerSum) {
			PowerTradeRecord powerTradeRecord = new PowerTradeRecord();
			String now = DateUtils.dateTimeNow(DateUtils.YYYY_MM_DD_HH_MM_SS);
			powerTradeRecord.setUuid(UuidUtils.getPrefixUUID(UuidPrefix.ENERGY_TRADE_RECORD));
			powerTradeRecord.setUserUuid(userUuid);
			powerTradeRecord.setEnergyBallUuid(decreaseRewardEnergyPowerBall.getUuid());
			powerTradeRecord.setInOrOut(ENEGY_OUT);
			powerTradeRecord.setPowerChange(powerSum);
			powerTradeRecord.setCreated(now);
			powerTradeRecord.setStatus(STATUS_DIE_OF_MINER);
			activityMapper.insertPowerTradeRecord(powerTradeRecord);
		}
		
	  /**
	   * @author Ming Yang
	   * @return 获取用户在特定时间获取的积分转换成的代币积累
	   */
	  public BigDecimal getUserTimePointToOas(String userUuid,String startTime,String endTime) {
		  BigDecimal totalPoint = energyBallMapper.selectUserPointByTime(userUuid, startTime, endTime);
		  BigDecimal nowRate=this.getNowRate();
		  if(totalPoint !=null) {
		  BigDecimal userTimePointToOas=totalPoint.multiply(nowRate);
		  return userTimePointToOas;
		  }else {
		  BigDecimal  userTimePointToOas=BigDecimal.ZERO;
		  return userTimePointToOas;
		  }
		 
	  }
	  
//	  /**
//	   * @author Ming Yang
//	   * @return 获取用户在特定时间获取的代币积累
//	   */
//	  public BigDecimal getUserTimeOas(String userUuid,String startTime,String endTime) {
//		  BigDecimal userTimeOas=userWalletTradeRecordMapper.selectUserPointToOas(userUuid, startTime, endTime);
//		  return userTimeOas;
//	  }
	  
	  /**
	   * @author Ming Yang
	   * @return   积分兑换代币的汇率
	   */
	  public BigDecimal getNowRate() {
		  String time=DateUtils.getYearMonth();
		  ExchangeRateModel exchangeRateModel=exchangeRateService.getRate(time, CurrencyCode.POINT);
		  Integer currency=exchangeRateModel.getCurrency();
		  BigDecimal rate=exchangeRateModel.getRate();
		  BigDecimal bigCurrency=new BigDecimal(currency);
		  BigDecimal toRate=bigCurrency.divide(rate);
		  
		  
//		  BigDecimal a=new BigDecimal(13.1235);
//		  BigDecimal b=new BigDecimal(21.234665);
//		  BigDecimal c=new BigDecimal(38.22345334);
//		  BigDecimal d=new BigDecimal(244.233456431);
//		  BigDecimal trade1=d.divideToIntegralValue(c);
//		  BigDecimal trade2=a.divideToIntegralValue(b);
//	      BigDecimal trade3=c.divideToIntegralValue(a);
//	      log.info("trade1:{}",trade1);
//	      log.info("trade2:{}",trade2);
//	      log.info("trade3:{}",trade3);
		  return toRate;
	  }
  
	/**
	 * @author Ming Yang
	 * @return immediatelyRewardSum
	 * @return 立即奖励代币额度
	 */
	public BigDecimal getImmediatelyRewardCionCount(PurchaseRecord purchaseRecord,BigDecimal i) {
		String rewardCoinName="代币";
		PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);	

		BigDecimal priceSum=purchaseRecord.getPriceSum();//一次购买矿机的总价格
		log.info("priceSum:{}",priceSum);
		BigDecimal frozenRatio=promotedRewardModel.getFrozenRatio();//冻结比例
		BigDecimal rewardRatio=promotedRewardModel.getRewardRatio();//返现总提成
		BigDecimal a=new BigDecimal(1);
		BigDecimal percentRatio=a.subtract(frozenRatio);//立即奖励代币的比例
		log.info("percentRatio:{}",percentRatio);
		//立即奖励代币额度 immediatelyRewardSum
		BigDecimal ImmediatelyRewardSum=(priceSum.multiply(rewardRatio)).multiply(percentRatio);
		log.info("ImmediatelyRewardSum:{}",ImmediatelyRewardSum);
		BigDecimal immediatelyRewardSum=ImmediatelyRewardSum.divide(i,18,BigDecimal.ROUND_HALF_UP);
		return immediatelyRewardSum;
	}
	
	public BigDecimal getPowerSum(PurchaseRecord purchaseRecord) {
		BigDecimal powerSum = BigDecimal.ZERO;
		Integer minerNum = purchaseRecord.getMinerNum();
		BigDecimal minerPower = purchaseRecord.getMinerPower();
		powerSum = minerPower.multiply(BigDecimal.valueOf((int)minerNum));			
		return powerSum;
	}
	
	/**
	 * @author Ming Yang
	 * @param purchaseRecord
	 * @param i
	 * @return    各级算力奖励提升
	 */
	public BigDecimal getPowerRewardCount(PurchaseRecord purchaseRecord,BigDecimal i) {
		String rewardCoinName="算力";
		PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);
		BigDecimal rewardRatio=promotedRewardModel.getRewardRatio();
		BigDecimal powerSum=this.getPowerSum(purchaseRecord);
		BigDecimal rewardPowerSum=powerSum.multiply(rewardRatio);
		BigDecimal toUserPowerReward=rewardPowerSum.divide(i,18,BigDecimal.ROUND_HALF_UP);
		return toUserPowerReward;
	}
	
	/**
	 * @author Ming Yang
	 * @param purchaseRecord
	 * @param i
	 * @return  单台矿机冻结奖励
	 */
	public BigDecimal getSingleFrozenRewardCoinCount(PurchaseRecord purchaseRecord,BigDecimal i) {
		String rewardCoinName="代币";
		PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);
		BigDecimal minerPrice = purchaseRecord.getMinerPrice();
		log.info("minerPrice:{}",minerPrice);
		BigDecimal frozenRatio=promotedRewardModel.getFrozenRatio();//冻结比例
		BigDecimal rewardRatio=promotedRewardModel.getRewardRatio();//返现总提成
		BigDecimal frozenRewardOas=(minerPrice.multiply(rewardRatio)).multiply(frozenRatio);
		return frozenRewardOas;
	}
	
	/**
	 * @author Ming Yang
	 * @param purchaseRecord
	 * @param userUuid
	 * @return userMaxMinerGrade
	 */
	public Integer getUserMaxMinerGrade(String userUuid) {
		List<PurchaseRecord> purchaseRecordList=minerMapper.selectByuserUuidMinerStatus(userUuid);
		if(purchaseRecordList!=null && purchaseRecordList.size()>0) {
		    Integer userMaxMinerGrade = purchaseRecordList.get(0).getMinerGrade();
		    return userMaxMinerGrade;
		}else {
			Integer userMaxMinerGrade = 0;//
			return userMaxMinerGrade;
		}	
	}
	
	/**
	 * @author Ming Yang
	 * @return 0 返回成功
	 * @return -1失败
	 */
	public Integer giveSuperiorsUserImmediatelyReward(PurchaseRecord purchaseRecord,String userUuid) {
		
		String rewardCoinName="代币";
		PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);
		UserModel userModel=userModelMapper.selectByUuid(userUuid);
		String userName=userModel.getName();
		log.info("userName:{}",userName);

		userName=this.getEncryptionUserName(userName);
//		double n=Math.pow(2,0);
//		BigDecimal N=new BigDecimal(n);
//		//购买矿机用户奖励代币
//		UserWallet buyUserWallet=userWalletMapper.selectByUserUuid(userUuid);
//		BigDecimal value=getImmediatelyRewardCionCount(purchaseRecord,N);
//		log.info("buyUserValue:{}",value);
//		log.info("buyUser增加余额:{}",userName);
//		userWalletMapper.increaseBalance(buyUserWallet.getUuid(), value);
//		log.info("buyUser增加记录:{}",userName);
//		UserWalletDetail userWalletDetail = userWalletService.setDetail(buyUserWallet,userName,UserWalletDetailScope.MINER_ADD_COIN,value,null,"测试下线购买矿机奖励",null,buyUserWallet.getBalance().add(value));
//		userWalletDetailMapper.insertSelective(userWalletDetail);

		Integer userMaxMinerGrade=this.getUserMaxMinerGrade(userUuid);
		log.info("buyUser最大矿机级别:{}",userMaxMinerGrade);
		//根据注册用户找到他的注册邀请码
		Integer inviteFrom=userModel.getInviteFrom();
		Integer maxN=promotedRewardModel.getMaxPromotedGrade();
		log.info("maxN:{}",maxN);
		if(inviteFrom != 0) {
			for(int i=1;i<maxN;i++) 
			{
				UserModel superiorsUserModel=userModelMapper.selectSuperiorsUserByInviteFrom(inviteFrom);
				if(superiorsUserModel !=null) {
				String superiorsName=superiorsUserModel.getName();
				log.info("superiorsName:{}",superiorsName);
				double superiorsn=Math.pow(2,i);
				BigDecimal superiorsN=new BigDecimal(superiorsn);
				log.info("superiorsN:{}",superiorsN);
				String superiorsUserUuid=superiorsUserModel.getUuid();
				Integer superiorsUserMaxMinerGrade=this.getUserMaxMinerGrade(superiorsUserUuid);
				log.info("superiorsUser最大矿机级别:{}",superiorsUserMaxMinerGrade);
				if(superiorsUserMaxMinerGrade>=userMaxMinerGrade) {
				UserWallet superiorsUserWallet=userWalletMapper.selectByUserUuid(superiorsUserUuid);
				BigDecimal superiorsValue=getImmediatelyRewardCionCount(purchaseRecord,superiorsN);
				log.info("superiorsValue:{}",superiorsValue);
				log.info("superiorsUser增加余额:{}",superiorsName);
				userWalletMapper.increaseBalance(superiorsUserWallet.getUuid(),superiorsValue);
				log.info("superiorsUser增加记录:{}",superiorsName);

				String remark="您推广下线购买矿机立即奖励总奖励的"+this.getOasRewardRatio()+",冻结总奖励的"+this.getOasFrozenRewardRatio()+",待下线矿机回本后一次性返还";
				UserWalletDetail superiorsUserWalletDetail = UserWalletService.setDetail(superiorsUserWallet,userName,UserWalletDetailScope.MINER_ADD_COIN,superiorsValue,null,remark,null,superiorsUserWallet.getBalance().add(superiorsValue));

				userWalletDetailMapper.insertSelective(superiorsUserWalletDetail);
				inviteFrom=superiorsUserModel.getInviteFrom();
				}else {
					inviteFrom=superiorsUserModel.getInviteFrom();
					continue;
				}
				log.info("nextInviteFrom:{}",inviteFrom);
				log.info("N:{}",i);
				}else {
					log.info("Nnext:{}",i);
					break;
				}
			}
			return 0;
		}else {
			return -1;
		}
		
	}
	
	/**
	 * @author Ming Yang
	 * @param purchaseRecord
	 * @param userUuid
	 * @return   返回冻结的单个矿机的奖励
	 */
	public Integer giveSuperiorsUserFrozenReward(PurchaseRecord purchaseRecord,String userUuid) {
		String rewardCoinName="代币";
		PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);
		UserModel userModel=userModelMapper.selectByUuid(userUuid);
		String userName=userModel.getName();
		log.info("userName:{}",userName);
		
//		double n=Math.pow(2,0);//2的幂次方
//		BigDecimal N=new BigDecimal(n);
		//最大反奖励等级用户
		Integer maxN=promotedRewardModel.getMaxPromotedGrade();
		log.info("maxN:{}",maxN);

//		//购买矿机用户奖励代币
//		UserWallet buyUserWallet=userWalletMapper.selectByUserUuid(userUuid);
//	    BigDecimal value=getSingleFrozenRewardCoinCount(purchaseRecord,N);
//	    log.info("buyUserValue:{}",value);
//		log.info("buyUser增加余额:{}",userName);
//		userWalletMapper.increaseBalance(buyUserWallet.getUuid(), value);
//		log.info("buyUser增加记录:{}",userName);
//		UserWalletDetail userWalletDetail = userWalletService.setDetail(buyUserWallet,userName,UserWalletDetailScope.FROZEN_ADD_COIN,value,null,"测试下线购买矿机奖励",null,buyUserWallet.getBalance().add(value));
//		userWalletDetailMapper.insertSelective(userWalletDetail);

		Integer userMaxMinerGrade=this.getUserMaxMinerGrade(userUuid);
		log.info("buyUser最大矿机级别:{}",userMaxMinerGrade);
		//根据注册用户找到他的注册邀请码
		Integer inviteFrom=userModel.getInviteFrom();
		
		if(inviteFrom != 0) {
			for(int i=1;i<maxN;i++) 
			{
				UserModel superiorsUserModel=userModelMapper.selectSuperiorsUserByInviteFrom(inviteFrom);
				if(superiorsUserModel !=null) {
				String superiorsName=superiorsUserModel.getName();
				log.info("superiorsName:{}",superiorsName);
				double superiorsn=Math.pow(2,i);
				BigDecimal superiorsN=new BigDecimal(superiorsn);
				log.info("superiorsN:{}",superiorsN);
				String superiorsUserUuid=superiorsUserModel.getUuid();
				Integer superiorsUserMaxMinerGrade=this.getUserMaxMinerGrade(superiorsUserUuid);
				log.info("superiorsUser最大矿机级别:{}",superiorsUserMaxMinerGrade);
				if(superiorsUserMaxMinerGrade>=userMaxMinerGrade) {
				UserWallet superiorsUserWallet=userWalletMapper.selectByUserUuid(superiorsUserUuid);
				BigDecimal superiorsValue=getSingleFrozenRewardCoinCount(purchaseRecord,superiorsN);
				log.info("superiorsValue:{}",superiorsValue);
				log.info("superiorsUser增加余额:{}",superiorsName);
				userWalletMapper.increaseBalance(superiorsUserWallet.getUuid(),superiorsValue);
				log.info("superiorsUser增加记录:{}",superiorsName);

				String remark="返还您推广下线购买矿机奖励冻结的"+this.getOasFrozenRewardRatio();
				UserWalletDetail superiorsUserWalletDetail = UserWalletService.setDetail(superiorsUserWallet,userName,UserWalletDetailScope.FROZEN_ADD_COIN,superiorsValue,null,remark,null,superiorsUserWallet.getBalance().add(superiorsValue));

				userWalletDetailMapper.insertSelective(superiorsUserWalletDetail);
				inviteFrom=superiorsUserModel.getInviteFrom();
				}else {
					inviteFrom=superiorsUserModel.getInviteFrom();
					continue;
				}
				log.info("nextInviteFrom:{}",inviteFrom);
				log.info("N:{}",i);
				}else {
					log.info("Nnext:{}",i);
					break;
				}
			}
			return 0;
		}else {
			return -1;
		}
	}
	
	public Integer giveSuperiorsUserPowerReward(PurchaseRecord purchaseRecord,String userUuid) {
		String rewardCoinName="算力";
		PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);
		UserModel userModel=userModelMapper.selectByUuid(userUuid);
		String userName=userModel.getName();
		log.info("userName:{}",userName);
		userName=this.getEncryptionUserName(userName);
		String updated = DateUtils.dateTimeNow(DateUtils.YYYYMMDDHHMMSS);
//		double n=Math.pow(2,0);//2的幂次方
//		BigDecimal N=new BigDecimal(n);
		//最大反奖励等级用户
		Integer maxN=promotedRewardModel.getMaxPromotedGrade();
		log.info("maxN:{}",maxN);
//		BigDecimal toBuyUserPowerRewardCount=this.getPowerRewardCount(purchaseRecord, N);
//		log.info("buyUserPower:{}",toBuyUserPowerRewardCount);
//		log.info("buyUser增加算力球:{}",userName);
//		this.addRewardPowerBall(purchaseRecord, userUuid, toBuyUserPowerRewardCount);
//		log.info("buyUser增加算力记录:{}",userName);
//		this.addRewardPowerTradeRecord(userUuid, toBuyUserPowerRewardCount);
//		log.info("buyUser增加算力奖励:{}",userName);
//		activityMapper.increasePower(userUuid, toBuyUserPowerRewardCount, updated);
		Integer userMaxMinerGrade=this.getUserMaxMinerGrade(userUuid);
		log.info("buyUser最大矿机级别:{}",userMaxMinerGrade);
		//根据注册用户找到他的注册邀请码
		Integer inviteFrom=userModel.getInviteFrom();
		
		if(inviteFrom != 0) {
			for(int i=1;i<maxN;i++) 
			{
				UserModel superiorsUserModel=userModelMapper.selectSuperiorsUserByInviteFrom(inviteFrom);
				if(superiorsUserModel !=null) {
				String superiorsName=superiorsUserModel.getName();
				log.info("superiorsName:{}",superiorsName);
				double superiorsn=Math.pow(2,i);
				BigDecimal superiorsN=new BigDecimal(superiorsn);
				log.info("superiorsN:{}",superiorsN);
				String superiorsUserUuid=superiorsUserModel.getUuid();
				Integer superiorsUserMaxMinerGrade=this.getUserMaxMinerGrade(superiorsUserUuid);
				log.info("superiorsUser最大矿机级别:{}",superiorsUserMaxMinerGrade);
				if(superiorsUserMaxMinerGrade>=userMaxMinerGrade) {
				BigDecimal toSuperiorsUserPowerRewardCount=this.getPowerRewardCount(purchaseRecord,superiorsN);
				log.info("superiorsUserPower:{}",toSuperiorsUserPowerRewardCount);
				log.info("superiorsUser增加算力球:{}",superiorsName);
				this.addRewardPowerBall(purchaseRecord,superiorsUserUuid,toSuperiorsUserPowerRewardCount);
				log.info("buyUser增加算力记录:{}",superiorsName);
				this.addRewardPowerTradeRecord(superiorsUserUuid,toSuperiorsUserPowerRewardCount);
				log.info("buyUser增加算力奖励:{}",superiorsName);
				activityMapper.increasePower(superiorsUserUuid,toSuperiorsUserPowerRewardCount,updated);
				inviteFrom=superiorsUserModel.getInviteFrom();
				}else {
					inviteFrom=superiorsUserModel.getInviteFrom();
					continue;
				}
				log.info("nextInviteFrom:{}",inviteFrom);
				log.info("N:{}",i);
				}else {
					log.info("Nnext:{}",i);
					break;
				}
			}
			return 0;
		}else {
			return -1;
		}
	}
	
	/**
	 * @author Ming Yang
	 * @param purchaseRecord
	 * @param userUuid
	 * @return   减少各级用户算力奖励
	 */
	public Integer decreaseSuperiorsUserPowerReward(PurchaseRecord purchaseRecord,String userUuid) {
		String rewardCoinName="算力";
		PromotedRewardModel promotedRewardModel = promotedRewardModelMapper.selectPromotedRewardByRewardName(rewardCoinName);
		UserModel userModel=userModelMapper.selectByUuid(userUuid);
		String userName=userModel.getName();
		log.info("userName:{}",userName);
		String updated = DateUtils.dateTimeNow(DateUtils.YYYYMMDDHHMMSS);
//		double n=Math.pow(2,0);//2的幂次方
//		BigDecimal N=new BigDecimal(n);
		//最大反奖励等级用户
		Integer maxN=promotedRewardModel.getMaxPromotedGrade();
		log.info("maxN:{}",maxN);
//		BigDecimal toBuyUserPowerRewardCount=this.getPowerRewardCount(purchaseRecord, N);
//		log.info("buyUserPower:{}",toBuyUserPowerRewardCount);
//		log.info("buyUser增加减少算力球:{}",userName);
//		this.addDecreaseRewardPowerBall(purchaseRecord, userUuid, toBuyUserPowerRewardCount);
//		log.info("buyUser增加算力记录:{}",userName);
//		this.addDecreaseRewardPowerTradeRecord(userUuid, toBuyUserPowerRewardCount);
//		log.info("buyUser减少算力奖励:{}",userName);
//		activityMapper.decreasePower(userUuid, toBuyUserPowerRewardCount, updated);
		//根据注册用户找到他的注册邀请码
		Integer inviteFrom=userModel.getInviteFrom();
		
		if(inviteFrom != 0) {
			for(int i=1;i<maxN;i++) 
			{
				UserModel superiorsUserModel=userModelMapper.selectSuperiorsUserByInviteFrom(inviteFrom);
				if(superiorsUserModel !=null){
				String superiorsName=superiorsUserModel.getName();
				log.info("superiorsName:{}",superiorsName);
				double superiorsn=Math.pow(2,i);
				BigDecimal superiorsN=new BigDecimal(superiorsn);
				log.info("superiorsN:{}",superiorsN);
				String superiorsUserUuid=superiorsUserModel.getUuid();
				BigDecimal toSuperiorsUserPowerRewardCount=this.getPowerRewardCount(purchaseRecord,superiorsN);
				log.info("superiorsUserPower:{}",toSuperiorsUserPowerRewardCount);
				log.info("superiorsUser增加减少算力球:{}",superiorsName);
				this.addDecreaseRewardPowerBall(purchaseRecord,superiorsUserUuid,toSuperiorsUserPowerRewardCount);
				log.info("buyUser增加算力记录:{}",superiorsName);
				this.addDecreaseRewardPowerTradeRecord(superiorsUserUuid,toSuperiorsUserPowerRewardCount);
				log.info("buyUser减少算力奖励:{}",superiorsName);
				activityMapper.decreasePower(superiorsUserUuid,toSuperiorsUserPowerRewardCount,updated);
				inviteFrom=superiorsUserModel.getInviteFrom();
				log.info("nextInviteFrom:{}",inviteFrom);
				log.info("N:{}",i);
				}else {
					log.info("Nnext:{}",i);
					break;
				}
			}
			return 0;
		}else {
			return -1;
		}
		
	}
}