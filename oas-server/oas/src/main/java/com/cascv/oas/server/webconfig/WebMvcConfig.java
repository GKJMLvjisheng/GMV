package com.cascv.oas.server.webconfig;


import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

 

/**

 * ͼƬ���Ե�ַ�������ַӳ��

 */

 

@SuppressWarnings("deprecation")
@Configuration

public class WebMvcConfig extends WebMvcConfigurerAdapter{

 

  @Override

  public void addResourceHandlers(ResourceHandlerRegistry registry){

//�ļ�����ͼƬurl ӳ��

//����server����·����handlerΪǰ̨���ʵ�Ŀ¼��locationsΪfiles���Ӧ�ı���·��

registry.addResourceHandler("/image/**").addResourceLocations("file:///D:/Temp/Image/");

System.out.print("--Config--");
  }
}

