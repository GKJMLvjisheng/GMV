package com.cascv.oas.server.timezone.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.timezone.model.CountryPromaryModel;

@Component
public interface CountryPromaryModelMapper {
	
	CountryPromaryModel selectTimeZoneByPromaryName(@Param("name") String name);
	
	CountryPromaryModel selectTimeZoneByCountryName(@Param("name") String name);
	
}