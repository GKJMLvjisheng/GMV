package com.gkyj.gmv.server.filter;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.gkyj.gmv.server.common.ShiroConstants;
import com.gkyj.gmv.server.shiro.OnlineSessionDAO;
import com.gkyj.gmv.server.user.model.OnlineSession;
import com.gkyj.gmv.server.user.model.UserModel;
import com.gkyj.gmv.server.utils.ShiroUtils;

// 
public class OnlineSessionFilter extends AccessControlFilter
{

    
    // 
    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    @Autowired
    private OnlineSessionDAO onlineSessionDAO;

    /**
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue)
            throws Exception
    {
        Subject subject = getSubject(request, response);
        if (subject == null || subject.getSession() == null) {
            return true;
        }
        Session session = onlineSessionDAO.readSession(subject.getSession().getId());
        if (session != null && session instanceof OnlineSession)  {
            OnlineSession onlineSession = (OnlineSession) session;
            request.setAttribute(ShiroConstants.ONLINE_SESSION, onlineSession);
            // 
            boolean isGuest = onlineSession.getUserUuid() == null;
            if (isGuest == true) {
                UserModel user = ShiroUtils.getUser();
                if (user != null) {
                    onlineSession.setUserUuid(user.getUuid());
                    onlineSession.setLoginName(user.getName());
                    onlineSession.markAttributeChanged();
                }
            }

            if (onlineSession.getStatus() == OnlineSession.OnlineStatus.off_line)
            {
                return false;
            }
        }
        return true;
    }

    
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception
    {
        Subject subject = getSubject(request, response);
        if (subject != null)
        {
            subject.logout();
        }
        saveRequestAndRedirectToLogin(request, response);
        return true;
    }

    @Override
    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException
    {
        WebUtils.issueRedirect(request, response, loginUrl);
    }

}
