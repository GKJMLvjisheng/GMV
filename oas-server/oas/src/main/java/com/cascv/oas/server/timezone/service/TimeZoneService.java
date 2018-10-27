package com.cascv.oas.server.timezone.service;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author Ming Yang
 * Date:20181013
 */
import org.springframework.stereotype.Service;

import com.cascv.oas.server.timezone.mapper.CountryPromaryModelMapper;
import com.cascv.oas.server.timezone.model.CountryPromaryModel;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TimeZoneService {
	@Autowired 
	private CountryPromaryModelMapper countryPromaryModelMapper;
	public String switchToUserTimeZoneId() {
		String dstTimeZoneId=null;
		String name=ShiroUtils.getAddress();
		log.info("name={}",name);
		if(name!=null && name !="") 
		{
			String [] arr = name.split("\\s+");
			String newName=arr[0];
			log.info("newName={}",newName);
			CountryPromaryModel countryPromaryModel=countryPromaryModelMapper.selectTimeZone(newName);
			if(countryPromaryModel!=null) {
				dstTimeZoneId=countryPromaryModel.getTimeZone();
				log.info("dstTimeZoneId={}",dstTimeZoneId);
				return dstTimeZoneId;
		}else
		{
			dstTimeZoneId="Asia/Shanghai";
			log.info("dstTimeZoneId={}",dstTimeZoneId);
			return dstTimeZoneId;
		}
	}else {
		dstTimeZoneId="Asia/Shanghai";
		log.info("dstTimeZoneId={}",dstTimeZoneId);
		return dstTimeZoneId;
		}
	}
}