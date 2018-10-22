package com.cascv.oas.server.filter;

import java.util.List;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class AccessFilter extends AuthorizationFilter{

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception {
        Subject subject = getSubject(request, response);

        //配置的访问所需角色
        String[] rolesArray = (String[]) mappedValue;
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
        //需要修改的地方在这里，判断访问所需的角色是否包含当前用户的角色
        List<String> roles = CollectionUtils.asList(rolesArray);
        boolean[] bo = subject.hasRoles(roles);
        //如果有一个角色满足，则可以访问
        for(boolean b : bo){
            if(b == true){
                return true;
            }
        }
        return false;
    }
}