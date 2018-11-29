package com.gkyj.gmv.server.load.config;



import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "media")
public class MediaConfig {
  @Setter @Getter private MediaServer server;
  
  @Bean
  public MediaServer getMediaServer() {
    return server;
  }
}
