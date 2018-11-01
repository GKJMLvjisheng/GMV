package com.cascv.oas.server.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.cascv.oas.server.common.ShiroConstants;
import com.cascv.oas.server.user.model.OnlineSession;

import lombok.extern.slf4j.Slf4j;

import com.cascv.oas.server.shiro.OnlineSessionDAO;


@Slf4j
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
        	log.info("synch to db {}", request.getServletContext().getContextPath());
            onlineSessionDAO.syncToDb(session);
        }
        return true;
    }
}
