package com.cascv.oas.server.reward.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.cascv.oas.server.reward.service.PromotedRewardService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImmediatelyRewardJob implements Job {
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("job immediately reward running\n");
		PromotedRewardService promotedRewardService = (PromotedRewardService) context.getJobDetail().getJobDataMap().get("promotedRewardService");

		if (promotedRewardService != null) {
			promotedRewardService.checkUserWhetherBuyMiner();
			promotedRewardService.giveUserPowerRewardBuyMiner();
		}
	}
}