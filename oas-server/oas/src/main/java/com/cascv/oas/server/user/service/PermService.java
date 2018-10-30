package com.cascv.oas.server.user.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cascv.oas.core.common.PermEntity;
import com.cascv.oas.server.user.mapper.RoleMenuMapper;
import com.cascv.oas.server.user.mapper.UserRoleModelMapper;
import com.cascv.oas.server.user.model.UserRole;
import com.cascv.oas.server.user.wrapper.RoleMenuViewModel;
import lombok.Getter;
import lombok.Setter;
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
	
	private Boolean getRewardByWalk=false;
	private Boolean getRewardByPromotion=false;
//	@Autowired
//	private RoleService roleService;
	
    public Set<String> getPermsByUserUuid(String uuid){
   	    Set<String> perms=new HashSet<>();    
        List<UserRole> userRoles=userRoleModelMapper.selectAllUserRole(uuid);
        
        /**多个角色
        Integer roleId;
        Set<Integer> roleIds=new HashSet<>();
        for(int j=0;j<userRoles.size();j++){
            roleId=userRoles.get(j).getRoleId();
            roleIds.add(roleId);
        }
        List<RoleMenuViewModel> rmList= new ArrayList<RoleMenuViewModel>();
        for(int z=0;z<roleIds.size();z++){
        	rmList.add(roleMenuMapper.selectAllRoleMenus(roleId).get(z));
        }
        for(int k=0;k<rmList.size();k++){
        	perms.add(rmList.get(k).getMenuName());
        }
        **/
    	        
    	Integer roleId=userRoles.get(0).getRoleId();
    	List<RoleMenuViewModel> rmList=roleMenuMapper.selectAllRoleMenus(roleId);
        for(int i=0;i<rmList.size();i++){
    		@SuppressWarnings("unused")
			String perm=rmList.get(i).getMenuName();
        	if(rmList.get(i).getMenuName().equals("划转")&&rmList.get(i).getMenuParentId()==5)
        		perm="在线钱包-划转";
        	else if(rmList.get(i).getMenuName().equals("划转")&&rmList.get(i).getMenuParentId()==8)
        		perm="交易钱包-划转";  
        	perms.add(perm);
        	log.info("perm={}",perm);
        }
        return perms;
    }
       
    /**
     * @category 行走/推广奖励的权限控制
     * @author lvjisheng
     * @param null
     * @return
     */
    public Boolean getWalkPerm(){
    	//针对用户角色
    	List<RoleMenuViewModel> rmList=roleMenuMapper.selectAllRoleMenus(2);
    	for(RoleMenuViewModel rm:rmList) {
    		getRewardByWalk=(rm.getMenuName().equals("计步"))? true:false;
    		if(getRewardByWalk)
    	    break;   		
    	}
    	    log.info("walk={}",getRewardByWalk);
        return getRewardByWalk;
    }
    public Boolean getPromotionPerm(){
    	//针对用户角色
    	List<RoleMenuViewModel> rmList=roleMenuMapper.selectAllRoleMenus(2);
    	for(RoleMenuViewModel rm:rmList) {	
    		getRewardByPromotion=(rm.getMenuName().equals("获得推广奖励"))? true:false;
    		if(getRewardByPromotion)
    		break;
    	}  
    	    log.info("promotion={}",getRewardByPromotion);
        return getRewardByPromotion;
    }
}