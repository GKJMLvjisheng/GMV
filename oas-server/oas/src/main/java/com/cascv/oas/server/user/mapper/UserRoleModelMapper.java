package com.cascv.oas.server.user.mapper;

import java.util.List;
import com.cascv.oas.server.user.model.UserRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserRoleModelMapper {
	
	List<UserRole> selectAllUserRole(String uuid);
	int insertUserRole(UserRole userRole); 
	int deleteUserRole(@Param("uuid") String uuid); 
}

