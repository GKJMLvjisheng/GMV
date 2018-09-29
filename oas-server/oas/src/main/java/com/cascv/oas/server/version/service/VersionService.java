package com.cascv.oas.server.version.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.server.news.config.MediaServer;
import com.cascv.oas.server.version.mapper.VersionModelMapper;
import com.cascv.oas.server.version.model.VersionModel;

@Service
public class VersionService {
	
	@Autowired
	private VersionModelMapper versionModelMapper;
	@Autowired
    private MediaServer mediaServer;
	
	public List<VersionModel> selectAllApps(){
		
		List<VersionModel> versionModelList = versionModelMapper.selectAllApps();
		  for (VersionModel versionModel : versionModelList) {
		    String fullLink = mediaServer.getImageHost() + versionModel.getAppUrl();
		    versionModel.setAppUrl(fullLink);
		  }
		  return versionModelList;
	}
	
	public List<VersionModel> selectAppByStableVersion(){
		
		List<VersionModel> versionModel = versionModelMapper.selectAllAppsByStableVersion();		  
		    String fullLink = mediaServer.getImageHost() + versionModel.get(0).getAppUrl();
		    versionModel.get(0).setAppUrl(fullLink);
		  
		  return versionModel;
	}
}