package com.cascv.oas.server.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.cascv.oas.server.common.ShiroConstants;
import com.cascv.oas.server.user.model.OnlineSession;
import com.cascv.oas.server.shiro.OnlineSessionDAO;

/**
 */
public class SyncOnlineSessionFilter extends PathMatchingFilter
{
    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    /**
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception
    {
        OnlineSession session = (OnlineSession) request.getAttribute(ShiroConstants.ONLINE_SESSION);
        // 
        if (session != null && session.getUserId() != null && session.getStopTimestamp() == null)
        {
            onlineSessionDAO.syncToDb(session);
        }
        return true;
    }
}
