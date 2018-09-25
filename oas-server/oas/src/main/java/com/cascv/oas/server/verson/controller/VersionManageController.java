package com.cascv.oas.server.verson.controller;
/**
 * @author Ming Yang
 */
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.verson.model.VersionModel;
import com.cascv.oas.server.verson.vo.VersionInfo;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Api(value="User Interface")
@RequestMapping(value="/api/v1/userCenter")
public class VersionManageController {
	
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
		
		String versionCode=versionInfo.getVersionCode();
		log.info("versionCode={}",versionCode);
		String str="/Apps/";
		String appUrl=str+fileName;
		
		versionModel.setVersionCode(versionCode);
		versionModel.setCreated(now);
		versionModel.setAppUrl(appUrl);
	
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
}