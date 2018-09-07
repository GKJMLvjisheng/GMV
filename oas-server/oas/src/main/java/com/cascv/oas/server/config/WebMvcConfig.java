package com.cascv.oas.server.config;

import org.apache.commons.lang.SystemUtils;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SuppressWarnings("deprecation")
@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter{

@Override

public void addResourceHandlers(ResourceHandlerRegistry registry){
	String SYSTEM_USER_HOME=SystemUtils.USER_HOME;
	String UPLOADED_FOLDER=SYSTEM_USER_HOME+"/Temp/Image/";
    registry.addResourceHandler("/image/**").addResourceLocations(UPLOADED_FOLDER);
System.out.print("--Config--");
  }
}
