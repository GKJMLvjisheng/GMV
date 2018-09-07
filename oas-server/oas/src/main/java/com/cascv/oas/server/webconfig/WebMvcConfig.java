package com.cascv.oas.server.webconfig;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.cascv.oas.server.news.controller.NewsController;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("deprecation")
@Configuration
@Slf4j
public class WebMvcConfig extends WebMvcConfigurerAdapter{

 

  @Override

  public void addResourceHandlers(ResourceHandlerRegistry registry){

	String SYSTEM_USER_HOME=SystemUtils.USER_HOME;
	String UPLOADED_FOLDER="file:///"+SYSTEM_USER_HOME+"/Temp/Image/";
	log.info("UPLOADED_FOLDER={}",UPLOADED_FOLDER);
    registry.addResourceHandler("/image/**").addResourceLocations(UPLOADED_FOLDER);
System.out.print("--Config--");
  }
}

