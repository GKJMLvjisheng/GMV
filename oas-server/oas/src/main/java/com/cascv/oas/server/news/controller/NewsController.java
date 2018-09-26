/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
package com.cascv.oas.server.news.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.news.model.NewsModel;
import com.cascv.oas.server.news.service.NewsService;
import lombok.extern.slf4j.Slf4j;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.SystemUtils;
@Slf4j
@RestController
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")
public class NewsController {
	
@Autowired
private NewsService newsService;
private static String SYSTEM_USER_HOME=SystemUtils.USER_HOME;

private static String UPLOADED_FOLDER =SYSTEM_USER_HOME
  +File.separator+"Temp"+File.separator+"Image" + File.separator+"news"+File.separator;

@PostMapping(value="/addNews")
@ResponseBody
public ResponseEntity<?> addNews(NewsModel newsInfo,@RequestParam(name="file",value="file",required=false) MultipartFile file){
	
	File dir=new File(UPLOADED_FOLDER);
  	 if(!dir.exists()){
  	   dir.mkdirs();
  	 }
   	
   	Map<String,String> info = new HashMap<>();

  if(file!=null)
  {
   	//生成唯一的文件名
   	String fileName = UUID.randomUUID().toString().replaceAll("-", "")+"-"+file.getOriginalFilename();
   	try 
   	{
           // Get the file and save it somewhere
           byte[] bytes = file.getBytes();
           Path path = Paths.get(UPLOADED_FOLDER + fileName);
           Files.write(path, bytes);
           String pictureName=fileName;
           log.info("pictureName={}",pictureName);            
           String str="/image/news/";
           String newsPicturePath=str+pictureName;
           
           NewsModel newsModel=new NewsModel();
          	
           newsModel.setNewsTitle(newsInfo.getNewsTitle());
           newsModel.setNewsAbstract(newsInfo.getNewsAbstract());
           newsModel.setNewsUrl(newsInfo.getNewsUrl());
           newsModel.setNewsPicturePath(newsPicturePath);
           newsModel.setCreated(DateUtils.getTime());
           
           newsService.addNews(newsModel);
           log.info("新闻进行了上传图片");
           return new ResponseEntity.Builder<NewsModel>()
        	  	      .setData(newsModel)
        	  	      .setErrorCode(ErrorCode.SUCCESS)
        	  	      .build();
       	}catch (Exception e)
   			{
       			log.info(" e.printStackTrace()={}");
       				e.printStackTrace();
       				return new ResponseEntity.Builder<Map<String, String>>()
       						.setData(info)
       						.setErrorCode(ErrorCode.GENERAL_ERROR)
       						.build();
   			}
   		}else
   			{
   			log.info("新闻未上传图片");
   			return new ResponseEntity.Builder<Integer>()
   					.setData(1)
   					.setErrorCode(ErrorCode.GENERAL_ERROR)
   					.build();
   			}
   	
}

@PostMapping(value="/updateNews")
@ResponseBody
public ResponseEntity<?> updateNews(NewsModel newsInfo,@RequestParam(name="file",value="file",required=false) MultipartFile file){
			log.info("--------start--------");
			NewsModel newsModel = new NewsModel();
			
			Map<String,String> info = new HashMap<>();
			
			newsModel.setNewsId(newsInfo.getNewsId());
			newsModel.setNewsTitle(newsInfo.getNewsTitle());
		    newsModel.setNewsAbstract(newsInfo.getNewsAbstract());
		    newsModel.setNewsUrl(newsInfo.getNewsUrl());    
	File dir=new File(UPLOADED_FOLDER);
	if(!dir.exists())
	{
		dir.mkdirs();
	}
	       
	 if(file!=null)
	 { 
		 log.info("11111");
	    //生成唯一的文件名
	   	String fileName = UUID.randomUUID().toString().replaceAll("-", "")+"-"+file.getOriginalFilename();
	      try 
	    	{
	            // Get the file and save it somewhere
	            byte[] bytes = file.getBytes();
	            Path path = Paths.get(UPLOADED_FOLDER + fileName);
	            Files.write(path, bytes);	            
	            String pictureName=fileName;
	            log.info("pictureName=",pictureName);            
	            String str="/image/news/";
	            String newsPicturePath=str+pictureName;
	            newsModel.setNewsPicturePath(newsPicturePath);
	            
	            newsService.updateNews(newsModel);
	            log.info("--------end-------");
      			return new ResponseEntity.Builder<Map<String, String>>()
      					.setData(info)
      					.setErrorCode(ErrorCode.SUCCESS)
      					.build();
	        } catch (Exception e)
	    		{
	        		log.info("修改失败"+e);
	           	    return new ResponseEntity.Builder<Integer>()
	               	      .setData(1)
	               	      .setErrorCode(ErrorCode.GENERAL_ERROR)
	               	      .build();
	    		}
	 		}else
	 			{
	 			log.info("newsInfo.getNewsPicturePath()={}",newsInfo.getNewsPicturePath());
	 			newsModel.setNewsPicturePath(newsInfo.getNewsPicturePath());
	 			newsService.updateNews(newsModel);
	 			log.info("--------end-------");
      			return new ResponseEntity.Builder<Map<String, String>>()
      					.setData(info)
      					.setErrorCode(ErrorCode.SUCCESS)
      					.build();
	 			}
}

@PostMapping(value="/selectAllNews")
@ResponseBody
public ResponseEntity<?> selectAllNews(){
  Map<String,Object> info=new HashMap<>();
  List<NewsModel> list=newsService.selectAllNews();
  int length=list.size();
  if(length>0) {
     info.put("list", list);
  }else
  {
    log.info("no news in mysql");
  }
    return new ResponseEntity.Builder<Map<String, Object>>()
              .setData(info).setErrorCode(ErrorCode.SUCCESS).build();
  }

@PostMapping(value="/deleteNews")
@ResponseBody
public ResponseEntity<?> deleteNews(@RequestBody NewsModel newsModel){
	
	Integer newsId=newsModel.getNewsId();
	log.info("newsId={}",newsId);
	
	newsService.deleteNews(newsId);
	
	return new ResponseEntity.Builder<Integer>()
			.setData(0).setErrorCode(ErrorCode.SUCCESS).build();
	
}

}
