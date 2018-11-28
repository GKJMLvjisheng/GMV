package com.gkyj.gmv.server.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.gkyj.gmv.server.common.ShiroConstants;
import com.gkyj.gmv.server.shiro.OnlineSessionDAO;
import com.gkyj.gmv.server.user.model.OnlineSession;

public class SyncOnlineSessionFilter extends PathMatchingFilter
{
    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    
    // @throws Exception
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        OnlineSession session = (OnlineSession) request.getAttribute(ShiroConstants.ONLINE_SESSION);
        // 
        if (session != null && session.getUserUuid() != null && session.getStopTimestamp() == null) {
            onlineSessionDAO.syncToDb(session);
        }
        return true;
    }
}
