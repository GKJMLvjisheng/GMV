package com.cascv.oas.server.user.mapper;
import com.cascv.oas.server.user.model.RoleMenu;
import com.cascv.oas.server.user.wrapper.RoleMenuViewModel;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface RoleMenuMapper {	
	 Integer insertRoleMenu(RoleMenu roleMenu);
	 List<RoleMenuViewModel> selectAllRoleMenus(@Param("role") Object role);
	 //List<RoleMenu> selectRoleMenuIdByColumnName(@Param("menuId") String menuId);
	 Integer deleteRoleMenu(@Param("roleMenuId") Integer roleMenuId);
	 Integer updateRoleMenu(RoleMenu roleMenu);
}

