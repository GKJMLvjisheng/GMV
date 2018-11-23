package com.cascv.oas.server.shiro.Mapper;

import java.util.List;
import org.springframework.stereotype.Component;
import com.cascv.oas.server.shiro.model.SysPermissionInit;

@Component
public interface SysPermissionInitMapper {
   
	List<SysPermissionInit> selectAll();
	
	
	
}

