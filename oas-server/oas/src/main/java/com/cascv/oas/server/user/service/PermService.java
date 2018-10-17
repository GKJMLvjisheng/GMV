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
	/**
     * 模拟根据用户id查询返回用户的所有权限，实际查询语句参考：
     * SELECT p.pval FROM perm p, role_perm rp, user_role ur
     * WHERE p.pid = rp.perm_id AND ur.role_id = rp.role_id
     * AND ur.user_id = #{userId}
     * @param uid
     * @return
     */
	@Autowired
	private RoleMenuMapper roleMenuMapper;
	@Autowired
	private UserRoleModelMapper userRoleModelMapper;
	
    public Set<String> getPermsByUserUuid(String uuid){
   	    Set<String> perms=new HashSet<>();    
        List<UserRole> userRoles=userRoleModelMapper.selectAllUserRole(uuid);
        for(int i=0;i<userRoles.size();i++){
        	Integer roleId=userRoles.get(i).getRoleId(); 
        	log.info("roleId={}",roleId);
        	List<RoleMenuViewModel> rmList=roleMenuMapper.selectAllRoleMenus(roleId);
        	perms.add(rmList.get(i).getMenuName());
        }
        return perms;
    }
}