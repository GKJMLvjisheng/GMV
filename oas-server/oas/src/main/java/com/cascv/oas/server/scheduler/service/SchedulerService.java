package com.cascv.oas.server.scheduler.service;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;


@Service
public class SchedulerService {
  @Autowired
  private SchedulerFactoryBean schedulerFactoryBean;
  public void addJob(JobDetail jobDetail, Trigger trigger) {
    Scheduler scheduler = schedulerFactoryBean.getScheduler();
    try {
      scheduler.scheduleJob(jobDetail, trigger);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
