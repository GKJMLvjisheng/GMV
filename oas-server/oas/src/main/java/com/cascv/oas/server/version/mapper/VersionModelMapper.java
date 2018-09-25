package com.cascv.oas.server.version.mapper;
/**
 * @author Ming Yang
 */
import java.util.List;

import org.springframework.stereotype.Component;

import com.cascv.oas.server.version.model.VersionModel;

@Component
public interface VersionModelMapper {
	
	 Integer insertApp(VersionModel versionModel);

	 Integer deleteApp(String uuid);
	 
	 Integer updateApp(VersionModel versionModel);
	 
	 List<VersionModel> selectAllApps();	 
	 
}