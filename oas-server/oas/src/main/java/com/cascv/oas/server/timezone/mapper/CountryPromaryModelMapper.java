package com.cascv.oas.server.timezone.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.cascv.oas.server.timezone.model.CountryPromaryModel;

@Component
public interface CountryPromaryModelMapper {
	
	CountryPromaryModel selectTimeZone(@Param("name") String name);	
	String inqurueTimeZone(@Param("timeZone") String [] timeZone);
}