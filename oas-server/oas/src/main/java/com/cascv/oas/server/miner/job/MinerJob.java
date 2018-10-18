package com.cascv.oas.server.miner.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.cascv.oas.server.miner.service.MinerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinerJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		log.info("updating minerStatus\n");
		MinerService minerService = (MinerService) context.getJobDetail().getJobDataMap().get("minerService");
		if (minerService != null) {
			minerService.updateMinerStatus();
		}
	}

}
