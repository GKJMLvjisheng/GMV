package com.gkyj.gmv.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
@EnableSwagger2
@EnableTransactionManagement
@MapperScan(basePackages = {"com.gkyj.gmv.server"})

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
}
