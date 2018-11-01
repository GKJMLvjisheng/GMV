package com.cascv.oas.server.shiro;

import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.web.session.mgt.WebSessionContext;
import org.springframework.stereotype.Component;
import com.cascv.oas.server.user.model.OnlineSession;
import com.cascv.oas.server.user.model.UserOnline;
import com.cascv.oas.core.utils.IpUtils;
import com.cascv.oas.core.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义sessionFactory会话
 */
@Component
@Slf4j
public class OnlineSessionFactory implements SessionFactory
{
    public Session createSession(UserOnline userOnline) {
    	log.info("OnlineSessionFactory create session, loginName-{}, session-{}", userOnline.getLoginName(), userOnline.getSessionId());
        OnlineSession onlineSession = userOnline.getSession();
        if (StringUtils.isNotNull(onlineSession) && onlineSession.getId() == null) {
        	log.info("OnlineSessionFactory create session set id {}", userOnline.getSessionId());
            onlineSession.setId(userOnline.getSessionId());
        }
        return userOnline.getSession();
    }

    @Override
    public Session createSession(SessionContext initData) {
      OnlineSession session = new OnlineSession();
      if (initData != null && initData instanceof WebSessionContext) {
		WebSessionContext sessionContext = (WebSessionContext) initData;
		HttpServletRequest request = (HttpServletRequest) sessionContext.getServletRequest();
		if (request != null) {
		    session.setHost(IpUtils.getIpAddr(request));
		    log.info("request path {}", request.getContextPath());
		}
		log.info("sessionContext {}", sessionContext.getSessionId());
		
		log.info("createSession id {} name {}", session.getId(), session.getLoginName());
      }
      return session;
   }
}
