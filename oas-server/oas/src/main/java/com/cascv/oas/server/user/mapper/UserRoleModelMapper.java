package com.cascv.oas.server.user.mapper;
/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
import java.util.List;

import com.cascv.oas.server.user.model.UserRole;

import org.springframework.stereotype.Component;

@Component
public interface UserRoleModelMapper {
	
	List<UserRole> selectAllUserRole(String uuid);
	 
}

