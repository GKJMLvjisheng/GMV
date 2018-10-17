package com.cascv.oas.server.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cascv.oas.server.user.mapper.RoleMenuMapper;
import com.cascv.oas.server.user.mapper.UserRoleModelMapper;
import com.cascv.oas.server.user.model.UserRole;
import com.cascv.oas.server.user.wrapper.RoleMenuViewModel;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class PermService {
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	@Autowired
	private UserRoleModelMapper userRoleModelMapper;
	
    public Set<String> getPermsByUserUuid(String uuid){
   	    Set<String> perms=new HashSet<>();    
        List<UserRole> userRoles=userRoleModelMapper.selectAllUserRole(uuid);
    	Integer roleId=userRoles.get(0).getRoleId();    	
    	List<RoleMenuViewModel> rmList=roleMenuMapper.selectAllRoleMenus(roleId);
        for(int i=0;i<rmList.size();i++){
    		@SuppressWarnings("unused")
			String perm=rmList.get(i).getMenuName();
        	if(rmList.get(i).getMenuName().equals("转账")&&rmList.get(i).getMenuParentId()==5){
        		perm="在线钱包-转账";
        	}else if(rmList.get(i).getMenuName().equals("转账")&&rmList.get(i).getMenuParentId()==8){
        		perm="交易钱包-转账";
        	}       	
        	perms.add(perm);
        	log.info("perm={}",perm);
        }
        return perms;
    }
}