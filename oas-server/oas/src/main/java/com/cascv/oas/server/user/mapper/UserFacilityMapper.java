package com.cascv.oas.server.user.mapper;

import com.cascv.oas.server.user.model.UserFacility;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserFacilityMapper {
	
	UserFacility inquireUserFacilityByModel(UserFacility userFacility);
	UserFacility inquireUserFacilityByIMEI(@Param("IMEI") String IMEI);
	int insertUserFacility(UserFacility userFacility); 
	int deleteUserFacility(@Param("uuid") String uuid); 
}

