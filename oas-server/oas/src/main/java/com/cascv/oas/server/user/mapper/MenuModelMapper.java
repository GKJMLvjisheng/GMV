package com.cascv.oas.server.user.mapper;

import java.util.List;
import com.cascv.oas.server.user.model.MenuModel;
import org.springframework.stereotype.Component;

@Component
public interface MenuModelMapper {
	 Integer insertParentMenu(MenuModel menuModel);
	 Integer insertChildrenMenu(MenuModel menuModel);
	 List<MenuModel> selectAllMenu();
	 Integer deleteMenu(String menuId);
	 Integer updateMenuInfo(MenuModel menuModel);
	 List<MenuModel> selectChildrenById(String menuId);
}

