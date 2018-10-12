package com.cascv.oas.server.scheduler.config;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "quartz")
public class SchedulerConfig {
  @Getter @Setter Properties properties;
  
  @Bean
  SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean  schedulerFactoryBean = new SchedulerFactoryBean();
    schedulerFactoryBean.setQuartzProperties(properties);
    return schedulerFactoryBean;
  }
}

