/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
package com.cascv.oas.server.news.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.server.news.model.NewsModel;
import com.cascv.oas.server.news.service.NewsService;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.wrapper.updateUserInfo;
import com.cascv.oas.server.utils.HostIpUtils;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;

@RestController
@Slf4j
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")
public class NewsController {
	
@Autowired
private NewsService newsService;
//获取本机IP地址
String localhostIp=HostIpUtils.getHostIp();
 	
private static String UPLOADED_FOLDER = "D:\\Temp\\Image\\news\\";

@PostMapping(value="/NewsManage")
public String index() {

    return "NewsManage";
}

@PostMapping(value="/AddNews")
@ResponseBody
public ResponseEntity<?> AddNews(NewsModel newsInfo,@RequestParam("file") MultipartFile file){
	
	File dir=new File(UPLOADED_FOLDER);
  	 if(!dir.exists()){
  	        dir.mkdirs();
  	    }
   	String msg="";
   	
   	Map<String,String> info = new HashMap<>();
   	
   	if (file.isEmpty()) 
   	{
   	   msg="文件为空";
       }

   	try 
   	{
           // Get the file and save it somewhere
           byte[] bytes = file.getBytes();
           Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
           Files.write(path, bytes);
           

           
           String  newsPicturePath=String.valueOf(path);
           
           log.info("--------获取路径--------:{}",newsPicturePath);
           
           String [] pictureArr=newsPicturePath.split("\\\\");
           String pictureName=pictureArr[pictureArr.length-1];
           
           log.info("pictureName={}",pictureName);            
           String str="http://"+localhostIp+":8080/image/news/";
           newsPicturePath=str+pictureName;
           log.info("newsPicturePath={}",newsPicturePath); 
	       NewsModel newsModel=new NewsModel();
	       	
	       newsModel.setNewsTitle(newsInfo.getNewsTitle());
	       newsModel.setNewsAbstract(newsInfo.getNewsAbstract());
	       newsModel.setNewsUrl(newsInfo.getNewsUrl());
           newsModel.setNewsPicturePath(newsPicturePath);
           
           newsService.addNews(newsModel);
           
       } catch (Exception e)
   		{
       	log.info(" e.printStackTrace()={}");
           e.printStackTrace();
   		}
   	
   	return new ResponseEntity.Builder<Map<String, String>>()
  	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
}

@PostMapping(value="/UpdateNews")
@ResponseBody
public ResponseEntity<?> UpdateNews(NewsModel newsInfo,@RequestParam(name="file",value="file",required=false) MultipartFile file){
			log.info("--------start--------");
			NewsModel newsModel = new NewsModel();
			
			Map<String,String> info = new HashMap<>();
			
			newsModel.setNewsId(newsInfo.getNewsId());
			newsModel.setNewsTitle(newsInfo.getNewsTitle());
		    newsModel.setNewsAbstract(newsInfo.getNewsAbstract());
		    newsModel.setNewsUrl(newsInfo.getNewsUrl());
	       
	      if(file!=null)
	      { 
	      try 
	    	{
	            // Get the file and save it somewhere
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
	            Files.write(path, bytes);
	            String newsPicturePath=String.valueOf(path);
	            
	            log.info("--------获取路径--------:{}",newsPicturePath);
	            String [] pictureArr=newsPicturePath.split("\\\\");
	            String pictureName=pictureArr[pictureArr.length-1];
	            
	                        
	            String str="http://"+localhostIp+":8080/image/news/";
	            newsPicturePath=str+pictureName;
	            log.info("newsPicturePath={}",newsPicturePath);
	            newsModel.setNewsPicturePath(newsPicturePath);
	            
	            newsService.updateNews(newsModel);
	            
	        } catch (Exception e)
	    		{
	        	log.info("修改失败"+e);
	    		}
}else
	{
	newsModel.setNewsPicturePath(newsInfo.getNewsPicturePath());
	newsService.updateNews(newsModel);
	}
	      

      log.info("--------end-------");
      
      return new ResponseEntity.Builder<Map<String, String>>()
      	      .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
      
    }

}
