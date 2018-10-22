package com.cascv.oas.server.version.controller;
/**
 * @author Ming Yang
 */
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
import com.cascv.oas.server.version.service.VersionService;
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
private VersionService versionService;

private static String SYSTEM_USER_HOME=SystemUtils.USER_HOME;
private static String UPLOADED_FOLDER =SYSTEM_USER_HOME+File.separator+"Temp"+File.separator+"Image" 
+File.separator+File.separator+"Apps"+File.separator;

@PostMapping(value="/upLoadApp")
@ResponseBody
public ResponseEntity<?> upLoadApp(VersionModel versionInfo,@RequestParam(name="file",value="file",required=false) MultipartFile file){
	
	File dir=new File(UPLOADED_FOLDER);
 	 if(!dir.exists()){
 	   dir.mkdirs();
 	 }

  	Map<String,String> info = new HashMap<>();
  	
  	if(file!=null) {
  		//日期时间生成唯一标识文件名
  		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
  		String fileName = format.format(new Date())+new Random().nextInt()+"-"+file.getOriginalFilename();
  		
  		try {
  	    // Get the file and save it somewhere
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        Files.write(path, bytes);
        
		VersionModel versionModel=new VersionModel();
		
		String str="/image/Apps/";
		String appUrl=str+fileName;
		log.info("appUrl={}",appUrl);
		log.info("status={}",versionInfo.getVersionStatus());
		
		versionModel.setVersionCode(versionInfo.getVersionCode());
	    versionModel.setVersionStatus(versionInfo.getVersionStatus());
	    versionModel.setAppUrl(appUrl);
	    versionModel.setUpGradeStatus(versionInfo.getUpGradeStatus());
	    versionModel.setCreated(DateUtils.getTime());
	   
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
	
	List<VersionModel> versionModels=versionService.selectAllApps();
	
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
public ResponseEntity<?> updateApp(VersionInfo versionInfo,@RequestParam(name="file",value="file",required=false) MultipartFile file){
	
	Map<String,String> info = new HashMap<>();
	VersionModel versionModel=new VersionModel();
	
	versionModel.setUuid(versionInfo.getUuid());
	versionModel.setVersionCode(versionInfo.getVersionCode());
	versionModel.setVersionStatus(versionInfo.getVersionStatus());
	versionModel.setUpGradeStatus(versionInfo.getUpGradeStatus());
	
	if(file!=null) {
		//日期时间生成唯一标识文件名
  		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
  		String fileName = format.format(new Date())+new Random().nextInt()+"-"+file.getOriginalFilename();
  		
  		try {
  	    // Get the file and save it somewhere
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        Files.write(path, bytes);
		
		String str="/image/Apps/";
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
  		
  		log.info("versionInfo.getAppUrl()={}",versionInfo.getAppUrl());
  		
  		versionModelMapper.updateApp(versionModel);
  		
		return new ResponseEntity.Builder<VersionModel>()
				.setData(versionModel)
				.setErrorCode(ErrorCode.SUCCESS)
				.build();
  		  }	
}

@PostMapping(value="/downloadApp")
@ResponseBody
public ResponseEntity<?> downloadApp(){
	if(versionService.selectAppByStableVersion().size()!=0) {
		
	List<VersionModel> stableVersionModel=versionService.selectAppByStableVersion();
	DownloadVersionInfo downloadVersionInfo=new DownloadVersionInfo();

	Integer versionCode=stableVersionModel.get(0).getVersionCode();
	downloadVersionInfo.setVersionCode(versionCode);
	String appUrl=stableVersionModel.get(0).getAppUrl();
	downloadVersionInfo.setAppUrl(appUrl);
	Integer upGradeStatus=stableVersionModel.get(0).getUpGradeStatus();
	downloadVersionInfo.setUpGradeStatus(upGradeStatus);
	return new ResponseEntity.Builder<DownloadVersionInfo>()
			.setData(downloadVersionInfo)
			.setErrorCode(ErrorCode.SUCCESS)
			.build();
	
	}else {
		
		return new ResponseEntity.Builder<Integer>()
				.setData(1)
				.setErrorCode(ErrorCode.GENERAL_ERROR)
				.build();
		}

}
}