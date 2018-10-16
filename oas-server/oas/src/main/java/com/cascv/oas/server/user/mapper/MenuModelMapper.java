package com.cascv.oas.server.user.mapper;

import java.util.List;
import com.cascv.oas.server.user.model.MenuModel;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface MenuModelMapper {
	 Integer insertParentMenu(MenuModel menuModel);
	 Integer insertChildrenMenu(MenuModel menuModel);
	 List<MenuModel> selectAllMenus();
	 Integer deleteMenu(@Param("menuId") Integer menuId);
	 Integer updateMenuOrderId(@Param("menuOrderId") Integer menuOrderId);
	 Integer updateMenu(MenuModel menuModel);
	 List<MenuModel> selectChildrenById(Integer menuId);
}

