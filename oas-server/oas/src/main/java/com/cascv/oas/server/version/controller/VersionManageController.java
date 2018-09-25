package com.cascv.oas.server.version.controller;
/**
 * @author Ming Yang
 */
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.commons.lang3.SystemUtils;
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
import com.cascv.oas.server.version.model.VersionModel;
import com.cascv.oas.server.news.config.MediaServer;
import com.cascv.oas.server.version.mapper.VersionModelMapper;
import com.cascv.oas.server.version.vo.DownloadVersionInfo;
import com.cascv.oas.server.version.vo.VersionInfo;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")
public class VersionManageController {

@Autowired
private VersionModelMapper versionModelMapper;
@Autowired
private MediaServer mediaServer;
	
private static String SYSTEM_USER_HOME=SystemUtils.USER_HOME;
private static String UPLOADED_FOLDER =SYSTEM_USER_HOME+File.separator+"Apps"+File.separator+"FirstVersion"+File.separator;
@PostMapping(value="/upLoadApp")
@ResponseBody
public ResponseEntity<?> upLoadApp(VersionInfo versionInfo,@RequestParam("file") MultipartFile file){
	File dir=new File(UPLOADED_FOLDER);
 	 if(!dir.exists()){
 	   dir.mkdirs();
 	 }

  	Map<String,String> info = new HashMap<>();
  	
  	if(file!=null) {
  		String fileName=file.getOriginalFilename();
  		
  		try {
  	    // Get the file and save it somewhere
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        Files.write(path, bytes);
        
		VersionModel versionModel=new VersionModel();
		String now=DateUtils.getTime();
		
		Integer versionCode=versionInfo.getVersionCode();
		log.info("versionCode={}",versionCode);
		String str=mediaServer.getImageHost()+"/Apps/FirstVersion/";
		String appUrl=str+fileName;
		log.info("appUrl={}",appUrl);
		log.info("status={}",versionInfo.getVersionStatus()); 
		versionModel.setVersionCode(versionCode);
		versionModel.setCreated(now);
		versionModel.setAppUrl(appUrl);
	    versionModel.setVersionStatus(versionInfo.getVersionStatus());
	    
	    versionModelMapper.insertApp(versionModel);
	    
		return new ResponseEntity.Builder<VersionModel>()
				.setData(versionModel)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
  	}catch (Exception e) {
  		String msg="error";
  		info.put("msg",msg);
  		log.info("文件上传失败，请重试！");
		return new ResponseEntity.Builder<Map<String, String>>()
				.setData(info)
				.setErrorCode(ErrorCode.GENERAL_ERROR)
				.build();
  		}
  	}else {
  		String msg="null";
  		info.put("msg",msg);
		return new ResponseEntity.Builder<Map<String, String>>()
				.setData(info)
				.setErrorCode(ErrorCode.GENERAL_ERROR)
				.build();
  		  }
}

@PostMapping(value="/selectAllApps")
@ResponseBody
public ResponseEntity<?> selectAllApps(){
	
	List<VersionModel> versionModels=versionModelMapper.selectAllApps();
	
	return new ResponseEntity.Builder<List<VersionModel>>()
			.setData(versionModels)
			.setErrorCode(ErrorCode.SUCCESS)
			.build();
	
}



@PostMapping(value="/deleteApp")
@ResponseBody
public ResponseEntity<?> deleteApp(@RequestBody VersionInfo versionInfo){
	
	versionModelMapper.deleteApp(versionInfo.getUuid());
	
	return new ResponseEntity.Builder<Integer>()
			.setData(0)
			.setErrorCode(ErrorCode.SUCCESS)
			.build();
	
}

@PostMapping(value="/updateApp")
@ResponseBody
public ResponseEntity<?> updateApp(VersionInfo versionInfo,@RequestParam("file") MultipartFile file){
	
	Map<String,String> info = new HashMap<>();
	VersionModel versionModel=new VersionModel();
	String now=DateUtils.getTime();
	
	versionModel.setVersionCode(versionInfo.getVersionCode());
	versionModel.setVersionStatus(versionInfo.getVersionStatus());
	versionModel.setCreated(now);
	
	if(file!=null) {
		
  		String fileName=file.getOriginalFilename();
  		
  		try {
  	    // Get the file and save it somewhere
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        Files.write(path, bytes);
		
		String str=mediaServer.getImageHost()+"/Apps/FirstVersion/";
		String appUrl=str+fileName;
		versionModel.setAppUrl(appUrl);
	    
		versionModelMapper.updateApp(versionModel);
		
		return new ResponseEntity.Builder<VersionModel>()
				.setData(versionModel)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
		
  	}catch (Exception e) {
  		String msg="error";
  		info.put("msg",msg);
  		log.info("文件修改失败，请重试！");
		return new ResponseEntity.Builder<Map<String, String>>()
				.setData(info)
				.setErrorCode(ErrorCode.GENERAL_ERROR)
				.build();
  		}
  	}else {
  		
  		versionModel.setAppUrl(versionModel.getAppUrl());
  		
  		versionModelMapper.updateApp(versionModel);
  		
		return new ResponseEntity.Builder<VersionModel>()
				.setData(versionModel)
				.setErrorCode(ErrorCode.GENERAL_ERROR)
				.build();
  		  }	
}

@PostMapping(value="/downloadApp")
@ResponseBody
public ResponseEntity<?> downloadApp(){
	
	DownloadVersionInfo downloadVersionInfo=new DownloadVersionInfo();
	
	Integer versionCode=2;
	downloadVersionInfo.setVersionCode(versionCode);
	String appUrl="http://18.219.19.160:8080/Apps/FirstVersion/App-release.apk";
	downloadVersionInfo.setAppUrl(appUrl);
	return new ResponseEntity.Builder<DownloadVersionInfo>()
			.setData(downloadVersionInfo)
			.setErrorCode(ErrorCode.SUCCESS)
			.build();
}
}