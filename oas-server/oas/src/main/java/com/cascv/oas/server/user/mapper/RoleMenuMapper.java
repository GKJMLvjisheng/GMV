package com.cascv.oas.server.user.mapper;
import com.cascv.oas.server.user.model.RoleMenu;
import com.cascv.oas.server.user.wrapper.RoleMenuViewModel;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface RoleMenuMapper {	
	 Integer insertRoleMenu(RoleMenu roleMenu);
	 Integer deleteRoleMenu(@Param("menuId") Integer menuId);
	 List<RoleMenuViewModel> selectAllRoleMenus(@Param("roleId") Integer roleId);
	 //List<RoleMenu> selectRoleMenuIdByColumnName(@Param("menuId") String menuId);
	 Integer deleteRoleMenu(RoleMenu roleMenu);
}

