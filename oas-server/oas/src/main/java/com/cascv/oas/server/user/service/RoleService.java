package com.cascv.oas.server.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cascv.oas.core.utils.DateUtils;
import com.cascv.oas.server.user.mapper.UserRoleModelMapper;
import com.cascv.oas.server.user.model.UserModel;
import com.cascv.oas.server.user.model.UserRole;

@Service
public class RoleService {
	
	@Autowired
	private UserRoleModelMapper userRoleModelMapper;


//	/**
//     * 模拟根据用户id查询返回用户的所有角色，实际查询语句参考：
//     * SELECT r.role_name FROM role_info r, user_role ur
//     * WHERE r.role_id = ur.role_id AND ur.uuid = #{uuid}
//     * @param uid
//     * @return
//     */
    public Set<String> getRolesByUserUuid(String uuid){
        Set<String> roles = new HashSet<>();
       List<UserRole> userRoles=userRoleModelMapper.selectAllUserRole(uuid); 
       for(int i=0;i<userRoles.size();i++)
       {   
    	   roles.add(userRoles.get(i).getRoleName());
       }       
       return roles;
    }  
    
    //更新用户的角色
    public void updateUerRoles(String uuid,Integer roleId){
        UserRole userRole=new UserRole();
        Set<String> roles=this.getRolesByUserUuid(uuid);
        if(roles!=null)
        	userRoleModelMapper.deleteUserRole(uuid);       
    	userRole.setUuid(uuid);
        userRole.setRoleId(roleId);
        userRole.setCreated(DateUtils.getTime());
        userRole.setRolePriority(1);
    	userRoleModelMapper.insertUserRole(userRole);
    }
}
