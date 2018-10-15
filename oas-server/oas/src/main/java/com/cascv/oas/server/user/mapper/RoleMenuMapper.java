package com.cascv.oas.server.user.mapper;
/*
* Name:NewsManage
* Author:yangming
* Date:2018.09.04
*/
import com.cascv.oas.server.user.model.RoleMenu;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface RoleMenuMapper {
	
	 Integer insertRoleMenu(RoleMenu roleMenu);
	 List<RoleMenu> selectAllRoleMenuId(@Param("roleId") String roleId);
	 List<RoleMenu> selectRoleMenuIdByColumnName(@Param("menuId") String menuId);
	 Integer deleteRoleMenu(String menuId);
	 Integer updateRoleMenuInfo(RoleMenu roleMenu);
}

