package com.cascv.oas.server;

import java.util.TimeZone;

import javax.annotation.PostConstruct;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cascv.oas.server.config.WebMvcConfig;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableSwagger2
@EnableTransactionManagement
@MapperScan(basePackages = {"com.cascv.oas.server"})
@Import(WebMvcConfig.class) 
public class App extends SpringBootServletInitializer{
  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
      return builder.sources(App.class);
  }

  public static void main(String[] args) {
    try {  SpringApplication.run(App.class, args);}
    catch(Exception e){
    	System.out.println(e);
    }
  }
  
  /**
   * @author Ming Yang
   * @see 设置默认启动时区
   */
  
  @PostConstruct
  void setDefaultTimezone() {
     TimeZone.setDefault(TimeZone.getTimeZone("Etc/GMT"));
     System.out.println("****默认时区 UTC****");
  }


}

