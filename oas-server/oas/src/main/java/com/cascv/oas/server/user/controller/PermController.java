package com.cascv.oas.server.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cascv.oas.core.common.ErrorCode;
import com.cascv.oas.core.common.ResponseEntity;
import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.user.mapper.MenuModelMapper;
import com.cascv.oas.server.user.mapper.RoleMenuMapper;
import com.cascv.oas.server.user.model.MenuModel;
import com.cascv.oas.server.user.model.RoleMenu;
import com.cascv.oas.server.user.service.RoleService;
import com.cascv.oas.server.user.wrapper.RoleMenuViewModel;
import com.cascv.oas.server.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;
@Controller
@Slf4j
public class PermController {
	@Autowired
	private MenuModelMapper menuModelMapper;
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	@Autowired
	private RoleService roleService;
	
    @PostMapping(value="/selectAllMenus")
	@ResponseBody
    public ResponseEntity<?> selectAllMenus()
    {
	       List<MenuModel> menuList =menuModelMapper.selectAllMenus();
	       Map<String,Object> info =new HashMap<>();
		   if(menuList.size()>0)
			{  info.put("menuList",menuList);
			   info.put("state", "success");
			}
		   else {
			   info.put("state", "failure");
		   }
		   return new ResponseEntity.Builder<Map<String,Object>>()
			  	      .setData(info)
			  	      .setErrorCode(ErrorCode.SUCCESS)
			  	      .build();
    }
    /**
     * @author lvjisheng
     * @param menuName,desc
     * @return
     */
    @PostMapping(value="/addParentMenu")
	@ResponseBody
    public ResponseEntity<?> addParentMenu(@RequestBody MenuModel menuModel)
    {      
	       Map<String,Object> info =new HashMap<>();
           try {       
           menuModel.setCreated(DateUtils.getTime());
           menuModel.setUpdated(DateUtils.getTime());
           menuModel.setDesc(menuModel.getDesc());
           menuModelMapper.insertParentMenu(menuModel);
           //在插入数据的同时同通过selectkey返回Id
           Integer menuId=menuModel.getMenuId();
           log.info("menuID={}",menuId);
		   menuModelMapper.updateMenuOrderId(menuId);
	       info.put("state","success");
           }catch(Exception e){
        	  log.info(e.getMessage());
           info.put("state","failure");
           }
		   return new ResponseEntity.Builder<Map<String,Object>>()
			  	      .setData(info)
			  	      .setErrorCode(ErrorCode.SUCCESS)
			  	      .build();
    }
    
    /**
     * @author lvjisheng
     * @param menuName,menuOrderId,menuParentName,menuId(as menuParentId),menuDesc
     * @return
     */
    @PostMapping(value="/addChildrenMenu")
	@ResponseBody
    public ResponseEntity<?> addChildrenMenu(@RequestBody MenuModel menuModel)
    {      MenuModel newMenuModel=new MenuModel();
	       Map<String,Object> info =new HashMap<>();	       
           try {     
           newMenuModel.setMenuName(menuModel.getMenuName());
           newMenuModel.setMenuParentName(menuModel.getMenuParentName());
           newMenuModel.setMenuParentId(menuModel.getMenuId());
           newMenuModel.setMenuOrderId(menuModel.getMenuOrderId());
           newMenuModel.setDesc(menuModel.getDesc());
           newMenuModel.setCreated(DateUtils.getTime());
           newMenuModel.setUpdated(DateUtils.getTime());
		   menuModelMapper.insertChildrenMenu(newMenuModel);
	       info.put("state","success");
           }catch(Exception e){
        	  log.info(e.getMessage());
           info.put("state","failure");
           }
		   return new ResponseEntity.Builder<Map<String,Object>>()
			  	      .setData(info)
			  	      .setErrorCode(ErrorCode.SUCCESS)
			  	      .build();
    }
    
    /**
     * @author lvjisheng
     * @param menuId
     * @return
     */
    @PostMapping(value="/deleteMenu")
 	@ResponseBody
     public ResponseEntity<?> deleteMenu(@RequestBody MenuModel menuModel)
     {     Integer menuId=menuModel.getMenuId();
 	       Map<String,Object> info =new HashMap<>();	       
           try {     
           menuModelMapper.deleteMenu(menuId);   
 	       info.put("state","success");
            }catch(Exception e){
         	  log.info(e.getMessage());
            info.put("state","failure");
            }
 		   return new ResponseEntity.Builder<Map<String,Object>>()
 			  	      .setData(info)
 			  	      .setErrorCode(ErrorCode.SUCCESS)
 			  	      .build();
     }
    
    /**
     * @author lvjisheng
     * @param menuId,menuName,menuDesc
     * @return
     */
    @PostMapping(value="/updateMenu")
 	@ResponseBody
     public ResponseEntity<?> updateMenu(@RequestBody MenuModel menuModel)
     {     //MenuModel newMenuModel=new MenuModel();
 	       Map<String,Object> info =new HashMap<>();	       
           try {
           menuModel.setUpdated(DateUtils.getTime());
           menuModelMapper.updateMenu(menuModel);   
 	       info.put("state","success");
            }catch(Exception e){
         	  log.info(e.getMessage());
            info.put("state","failure");
            }
 		   return new ResponseEntity.Builder<Map<String,Object>>()
 			  	      .setData(info)
 			  	      .setErrorCode(ErrorCode.SUCCESS)
 			  	      .build();
     }
    
    
    /*************************************权限管理******************************************************/       
    /**
     * @author lvjisheng
     * @param null
     * @return
     */  
    @PostMapping(value="/selectAllRoleMenus")
 	@ResponseBody
     public ResponseEntity<?> selectAllRoleMenus()
     {
	 	   String uuid=ShiroUtils.getUser().getUuid();
	 	   Set<String> roles=roleService.getRolesByUserUuid(uuid);
	 	   //暂时每个用户只有一个角色
//	 	   for(String role:roles) {
//	 	   }
	 	   List<String> list = new ArrayList(roles);
	 	   log.info("role={}",list.get(0));
    	   List<RoleMenuViewModel> rmList =roleMenuMapper.selectAllRoleMenus(list.get(0));
 	       Map<String,Object> info =new HashMap<>();
 		   if(rmList.size()>0) 			   
 			{  info.put("menuList",rmList);
 			   info.put("state", "success");
 			}
 		   else {
 			   info.put("state", "failure");
 		   }
 		   return new ResponseEntity.Builder<Map<String,Object>>()
 			  	      .setData(info)
 			  	      .setErrorCode(ErrorCode.SUCCESS)
 			  	      .build();
     }
    
    /**
     * @author lvjisheng
     * @param roleId,menuId
     * @return
     */
    @PostMapping(value="/addRoleMenu")
	@ResponseBody
    public ResponseEntity<?> addRoleMenu(@RequestBody RoleMenu roleMenu)
    {      
	       Map<String,Object> info =new HashMap<>();
           try {       
	           roleMenu.setCreated(DateUtils.getTime());
	           roleMenuMapper.insertRoleMenu(roleMenu);
		       info.put("state","success");
           }catch(Exception e){
	           log.info(e.getMessage());
	           info.put("state","failure");
           }
		   return new ResponseEntity.Builder<Map<String,Object>>()
			  	      .setData(info)
			  	      .setErrorCode(ErrorCode.SUCCESS)
			  	      .build();
    }
    
    
    /**
     * @author lvjisheng
     * @param roleMenuId
     * @return
     */
    @PostMapping(value="/deleteRoleMenu")
 	@ResponseBody
     public ResponseEntity<?> deleteRoleMenu(@RequestBody RoleMenu rm)
     {     
 	       Map<String,Object> info =new HashMap<>();
            try {   
    	            roleMenuMapper.deleteRoleMenu(rm.getRoleMenuId());
    	 	        info.put("state","success");	            
            }catch(Exception e){
	         	    log.info(e.getMessage());
	                info.put("state","failure");
            }
 		   return new ResponseEntity.Builder<Map<String,Object>>()
 			  	      .setData(info)
 			  	      .setErrorCode(ErrorCode.SUCCESS)
 			  	      .build();
     }    
}
