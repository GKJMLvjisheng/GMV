package com.cascv.oas.server.version.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.news.config.MediaServer;
import com.cascv.oas.server.news.service.NewsService;
import com.cascv.oas.server.timezone.service.TimeZoneService;
import com.cascv.oas.server.version.mapper.VersionModelMapper;
import com.cascv.oas.server.version.model.VersionModel;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class VersionService {
	
	@Autowired
	private VersionModelMapper versionModelMapper;
	@Autowired
    private MediaServer mediaServer;
	@Autowired 
	private TimeZoneService timeZoneService;
	public List<VersionModel> selectAllApps(){
		
		List<VersionModel> versionModelList = versionModelMapper.selectAllApps();
		  for (VersionModel versionModel : versionModelList) {
			  String srcFormater="yyyy-MM-dd HH:mm:ss";
			  String dstFormater="yyyy-MM-dd HH:mm:ss";
			  String dstTimeZoneId=timeZoneService.switchToUserTimeZoneId();
		      String created=DateUtils.string2Timezone(srcFormater, versionModel.getCreated(), dstFormater, dstTimeZoneId);
		      String fullLink = mediaServer.getImageHost() + versionModel.getAppUrl();
		      versionModel.setCreated(created);
		      versionModel.setAppUrl(fullLink);
		      log.info("newCreated={}",created);
		  }
		  return versionModelList;
	}
	
	public List<VersionModel> selectAppByStableVersion(){
		
		List<VersionModel> versionModel = versionModelMapper.selectAllAppsByStableVersion();
		if(versionModel != null && versionModel.size() != 0) {
		    String fullLink = mediaServer.getImageHost() + versionModel.get(0).getAppUrl();
		    versionModel.get(0).setAppUrl(fullLink);
		}
		  return versionModel;
	}
}