package com.cascv.oas.server.reward.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import com.cascv.oas.server.reward.service.PromotedRewardService;
import com.cascv.oas.server.user.service.PermService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RewardJob implements Job {
//	@Autowired
//	private PermService permService;
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("job detection buy miner running\n");
		PromotedRewardService promotedRewardService = (PromotedRewardService) context.getJobDetail().getJobDataMap().get("promotedRewardService");
//		boolean flag = permService.getPromotionPerm();//返回一个boolean,若为true,则获取奖励
		if (promotedRewardService != null) {
//            if(flag==true)
			promotedRewardService.checkUserWhetherBuyMiner();
			promotedRewardService.giveUserPowerRewardBuyMiner();
			promotedRewardService.decreaseUserPowerRewardBuyMiner();
//			promotedRewardService.checkBuyUserMinerRedeem();
//            else
//            log.info("no permission to do reward job");
		}
	}
}