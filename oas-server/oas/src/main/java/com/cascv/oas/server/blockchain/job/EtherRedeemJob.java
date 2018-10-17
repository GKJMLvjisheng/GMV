package com.cascv.oas.server.blockchain.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.cascv.oas.server.blockchain.service.EthWalletService;
import com.cascv.oas.server.miner.service.MinerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EtherRedeemJob implements Job {

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// log.info("job ethRedeem running\n");
		EthWalletService ethWalletService = (EthWalletService) context.getJobDetail().getJobDataMap().get("service");
		if (ethWalletService != null) {
			// ethWalletService.updateJob();
		}
		log.info("updating minerStatus\n");
		MinerService minerService = (MinerService) context.getJobDetail().getJobDataMap().get("minerService");
		if (minerService != null) {
			minerService.updateMinerStatus();
		}
	}

}
