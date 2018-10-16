package com.cascv.oas.server.user.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

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
    public Set<String> getPermsByUserUuid(String userUuid){
        Set<String> perms = new HashSet<>();
        
        perms.add("html:edit");
        //c++程序员的权限
        perms.add("hardware:debug");
        //java程序员的权限
        perms.add("mvn:install");
        perms.add("mvn:clean");
        perms.add("mvn:test");
        
        perms.add("userInfo:upload");
        return perms;
    }
}
