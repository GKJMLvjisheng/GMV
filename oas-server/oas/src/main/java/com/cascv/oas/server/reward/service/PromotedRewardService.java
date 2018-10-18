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
import com.cascv.oas.server.blockchain.job.EtherRedeemJob;
import com.cascv.oas.server.blockchain.mapper.UserWalletDetailMapper;
import com.cascv.oas.server.blockchain.mapper.UserWalletMapper;
import com.cascv.oas.server.blockchain.model.UserWallet;
import com.cascv.oas.server.blockchain.model.UserWalletDetail;
import com.cascv.oas.server.blockchain.service.UserWalletService;
import com.cascv.oas.server.common.UserWalletDetailScope;
import com.cascv.oas.server.miner.mapper.MinerMapper;
import com.cascv.oas.server.miner.model.PurchaseRecord;
import com.cascv.oas.server.reward.job.RewardJob;
import com.cascv.oas.server.reward.mapper.PromotedRewardModelMapper;
import com.cascv.oas.server.reward.model.PromotedRewardModel;
import com.cascv.oas.server.scheduler.service.SchedulerService;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.user.mapper.UserModelMapper;
import com.cascv.oas.server.user.model.UserModel;

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
	private UserWalletService userWalletService;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired 
	private UserWalletDetailMapper userWalletDetailMapper;
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
	    JobDetail jobDetail = JobBuilder.newJob(RewardJob.class)
	        .withIdentity("JobDetailC", "groupC").build();
	    Trigger trigger = TriggerBuilder.newTrigger().withIdentity("triggerC", "groupC")
	        .withSchedule(SimpleScheduleBuilder.simpleSchedule()
	            .withIntervalInSeconds(30).repeatForever()).startNow().build();
	    jobDetail.getJobDataMap().put("promotedRewardService", this);
	    schedulerService.addJob(jobDetail, trigger);
	    log.info("add reward job ...");
	  }
	  
	  public synchronized void checkUserWhetherBuyMiner() {
		  log.info(" check all users whether buy miner");
		  List<PurchaseRecord> purchaseRecordList=minerMapper.selectByMinerPurchaseStatus();
		  if (purchaseRecordList != null && purchaseRecordList.size() > 0) {
			  for(PurchaseRecord purchaseRecord:purchaseRecordList) {
				  String userUuid=purchaseRecord.getUserUuid();
				  this.giveSuperiorsUserImmediatelyReward(purchaseRecord, userUuid);
				  minerMapper.updateByMinerPurchaseStatus(purchaseRecord);
				  log.info("end reward job ...");
			  }
		  }
	  }
	  
	/**
	 * @author Ming Yang
	 * @return immediatelyRewardSum
	 * @return 立即奖励代币额度
	 */
	public BigDecimal getImmediatelyReardCionCount(PurchaseRecord purchaseRecord,BigDecimal i) {
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
	
	/**
	 * @author Ming Yang
	 * @param purchaseRecord
	 * @param userUuid
	 * @return userMaxMinerGrade
	 */
	public Integer getUserMaxMinerGrade(String userUuid) {
		List<PurchaseRecord> purchaseRecordList=minerMapper.selectByuserUuidMinerStatus(userUuid);
		if(purchaseRecordList.size()>0) {
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
		double n=Math.pow(2,0);
		BigDecimal N=new BigDecimal(n);
		//购买矿机用户奖励代币
		UserWallet buyUserWallet=userWalletMapper.selectByUserUuid(userUuid);
		BigDecimal value=getImmediatelyReardCionCount(purchaseRecord,N);
		log.info("buyUserValue:{}",value);
		log.info("buyUser增加余额:{}",userName);
		userWalletMapper.increaseBalance(buyUserWallet.getUuid(), value);
		log.info("buyUser增加记录:{}",userName);
		UserWalletDetail userWalletDetail = userWalletService.setDetail(buyUserWallet,userName,UserWalletDetailScope.MINER_ADD_COIN,value,null,"测试下线购买矿机奖励",null);
		userWalletDetailMapper.insertSelective(userWalletDetail);
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
				BigDecimal superiorsValue=getImmediatelyReardCionCount(purchaseRecord,superiorsN);
				log.info("superiorsValue:{}",superiorsValue);
				log.info("superiorsUser增加余额:{}",superiorsName);
				userWalletMapper.increaseBalance(superiorsUserWallet.getUuid(),superiorsValue);
				log.info("superiorsUser增加记录:{}",superiorsName);
				UserWalletDetail superiorsUserWalletDetail = userWalletService.setDetail(superiorsUserWallet,userName,UserWalletDetailScope.MINER_ADD_COIN,superiorsValue,null,"测试下线购买矿机奖励",null);
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
}